package com.example.combination.repository;


import com.example.combination.domain.member.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }
    
    //PK로 DB가 회원 조회
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
        
    }//userId로 회원 단건 조회
    public Optional<Member> findByUserId(String userId) {
        List<Member> result = em.createQuery("select m from Member m where m.userId =:userId", Member.class)
                .setParameter("userId", userId)
                .getResultList();
        return result.stream().findFirst();
    }

    //nickname 조회
    public Optional<Member> findByNickname(String nickname) { //em.find()는 PK만 조회가능. 이외에는 JPQL
        List<Member> result = em.createQuery("select m from Member m where m.userInfo.nickname =:nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList();
        return result.stream().findFirst();
    }

    //회원 엔티티에서 전체 회원 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
    
    //회원 이름으로 회원 리스트 조회
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
