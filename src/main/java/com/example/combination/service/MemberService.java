package com.example.combination.service;


import com.example.combination.domain.member.LogInStatus;
import com.example.combination.domain.member.Member;
import com.example.combination.domain.member.MemberStatus;
import com.example.combination.exception.MemberNotFoundException;
import com.example.combination.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    //회원가입
    private final MemberRepository memberRepository;


    @Transactional
    public Long join(Member member) {
        validateDuplicateUserId(member.getUserId());
        memberRepository.save(member);
        return member.getId();
    }
    //중복 회원 검증
    public void validateDuplicateUserId(String userId) { //unique
        memberRepository.findByUserId(userId)
                .ifPresent(member ->  { //Optional + ifPresent
                    if (member.getUserId().equals(userId)) {

                    }
                        throw new IllegalStateException("이미 존재하는 회원 ID 입니다.: " + userId);
                });
    }
    //회원 상태 변경 (회원 가입/회원 탈퇴)
    @Transactional
    public void updateMemberStatus(String userId, MemberStatus newStatus) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()-> new MemberNotFoundException(userId));
        member.changeMemberStatus(newStatus);
    }
    //회원 상태 변경 (로그인/로그아웃)
    @Transactional
    public void updateMemberStatus(String userId, LogInStatus newStatus) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()->new MemberNotFoundException(userId));
        member.changeLogInStatus(newStatus);
    }

    //중복 닉네임 검증
    @Transactional
    public void validateNickname(String nickname) {
        validateDuplicateNickname(nickname);
        memberRepository.findByNickname(nickname);
    }

    public void validateDuplicateNickname(String nickname) {
        memberRepository.findByNickname(nickname)
                .ifPresent(member ->  {
                        throw new IllegalStateException("사용할 수 없는 닉네임 입니다.: " + nickname);
                    });
                }
    }







//    //중복 회원 검증
//    private void validateDuplicateMember(Member member) {
//        List<Member> findMembers = memberRepository.findByName(member.getName());
//        if(!findMembers.isEmpty())
//            throw new IllegalStateException("중복된 회원으로 아이디를 사용할 수 없습니다.");
//    }
//
//    /*회원 조회*/
//
//    //전체 회원 조회
//    public List<Member> findMembers() {
//        return memberRepository.findAll();
//    }
//
//
//    //회원 이름으로 회원 조회
//    public List<Member> findByName(String name) {
//        return memberRepository.findByName(name);
//    }


