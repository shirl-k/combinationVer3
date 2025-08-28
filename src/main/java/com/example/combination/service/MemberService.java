package com.example.combination.service;


import com.example.combination.domain.member.Member;
import com.example.combination.domain.member.MemberStatus;
import com.example.combination.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    //회원가입
    private final MemberRepository memberRepository;


    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }
    
    //중복 회원 검증
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty())
            throw new IllegalStateException("중복된 회원으로 아이디를 사용할 수 없습니다.");
    }

    /*회원 조회*/
    
    //전체 회원 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    
    //userId로 회원 단건 조회
    public Optional<Member> findOne(String userId) {
        return memberRepository.findOne(userId);
    }

    //회원 이름으로 회원 조회
    public List<Member> findByName(String name) {
        return memberRepository.findByName(name);
    }

    @Transactional
    public void updateMemberStatus(String userId , MemberStatus newStatus) {
        Member member = memberRepository.findOne(userId)
                .orElseThrow(()-> new IllegalStateException("회원이 존재하지 않습니다."));
        member.setMemberStatus(newStatus);
    }


}
