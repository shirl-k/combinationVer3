package com.example.combination.repository;

import com.example.combination.domain.delivery.JustDelivery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JustDeliveryRepository {

    private final EntityManager em;


    public void save (JustDelivery justDelivery) {
        em.persist(justDelivery);
    }

    //배송만 서비스 - 배송건 ID로 배송 정보 조회
    public Optional<JustDelivery> findByJustId(Long id) {
        return Optional.ofNullable(em.find(JustDelivery.class, id));
    }

    //배송만 서비스 전체 배송정보 조회
    public List<JustDelivery> findAllByJustId(Long id) {
        return em.createQuery("select j from JustDelivery j",JustDelivery.class)
                .getResultList();
    }
}
