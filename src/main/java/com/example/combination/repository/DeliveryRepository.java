package com.example.combination.repository;

import com.example.combination.domain.delivery.DeliveryForm;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class DeliveryRepository {

    private final EntityManager em;


    public void save(DeliveryForm deliveryForm) {
        em.persist(deliveryForm);
    }
    //배송건 ID로 배송 정보 조회
    public Optional<DeliveryForm> findByDeliveryId(Long deliveryId) {
        return Optional.ofNullable(em.find(DeliveryForm.class, deliveryId));
    }
    //전체 배송건 정보 조회
    public List<DeliveryForm> findAll() {
        return em.createQuery("select d from DeliveryForm d", DeliveryForm.class)
                .getResultList();
    }

}
