package com.example.combination.service;

import com.example.combination.domain.member.Member;
import com.example.combination.domain.member.MemberStatus;
import com.example.combination.domain.member.SocialAccount;
import com.example.combination.domain.order.ShoppingCart;
import com.example.combination.repository.MemberRepository;
import com.example.combination.repository.SocialAccountRepository;
import com.example.combination.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final SocialAccountRepository socialAccountRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());
        
        if (oAuth2UserInfo.getEmail() == null) {
            throw new OAuth2AuthenticationException("이메일 정보를 가져올 수 없습니다.");
        }

        Member member = processOAuth2User(oAuth2UserInfo, registrationId);
        return new CustomOAuth2User(member, oAuth2User.getAttributes());
    }

    private Member processOAuth2User(OAuth2UserInfo oAuth2UserInfo, String registrationId) {
        // 기존 소셜 계정으로 가입된 회원이 있는지 확인
        SocialAccount socialAccount = socialAccountRepository.findByProviderAndProviderId(registrationId, oAuth2UserInfo.getId())
                .orElse(null);

        if (socialAccount != null) {
            // 기존 회원 로그인
            Member member = socialAccount.getMember();
            updateExistingUser(member, oAuth2UserInfo);
            return member;
        } else {
            // 이메일로 기존 회원이 있는지 확인
            Member existingMember = memberRepository.findByUserId(oAuth2UserInfo.getEmail())
                    .orElse(null);

            if (existingMember != null) {
                // 기존 회원에 소셜 계정 연결
                createSocialAccount(existingMember, oAuth2UserInfo, registrationId);
                return existingMember;
            } else {
                // 신규 회원 가입
                return createNewUser(oAuth2UserInfo, registrationId);
            }
        }
    }

    private Member createNewUser(OAuth2UserInfo oAuth2UserInfo, String registrationId) {
        Member member = Member.builder()
                .name(oAuth2UserInfo.getName())
                .userId(oAuth2UserInfo.getEmail())
                .memberStatus(MemberStatus.ACTIVE)
                .totalSpent(0)
                .availablePoints(0)
                .usedPoints(0)
                .usePoints(0)
                .build();

//        member = memberRepository.save(member); Incompatible types; void cannot be converted to Member 
            //MemberRepository에 void save 메서드 - 회원 저장 - 컴파일 에러
        
        // 소셜 계정 정보 저장
        createSocialAccount(member, oAuth2UserInfo, registrationId);
        
        // 기본 장바구니 생성
        ShoppingCart defaultCart = ShoppingCart.createDefaultCart(member);
        member.addShoppingCart(defaultCart);
        shoppingCartRepository.save(defaultCart);
        
        log.info("신규 회원 가입: {}", member.getUserId());
        return member;
    }

    private void updateExistingUser(Member member, OAuth2UserInfo oAuth2UserInfo) {
        if (!member.getName().equals(oAuth2UserInfo.getName())) {
            member.setName(oAuth2UserInfo.getName());
            memberRepository.save(member);
        }
    }

    private void createSocialAccount(Member member, OAuth2UserInfo oAuth2UserInfo, String registrationId) {
        SocialAccount socialAccount = SocialAccount.builder()
                .member(member)
                .provider(registrationId)
                .providerId(oAuth2UserInfo.getId())
                .email(oAuth2UserInfo.getEmail())
                .name(oAuth2UserInfo.getName())
                .build();

        socialAccountRepository.save(socialAccount);
    }
}
