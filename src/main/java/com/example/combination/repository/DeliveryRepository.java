package com.example.combination.repository;

import com.example.combination.domain.delivery.DeliveryAddressForm;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class DeliveryRepository {

    private final EntityManager em;


    public void save(DeliveryAddressForm deliveryAddressForm) {
        em.persist(deliveryAddressForm);
    }
    //배송건 ID로 배송 정보 조회
    public Optional<DeliveryAddressForm> findById(Long deliveryId) {
        return Optional.ofNullable(em.find(DeliveryAddressForm.class, deliveryId));
    }
    //전체 배송건 정보 조회
    public List<DeliveryAddressForm> findAll() {
        return em.createQuery("select d from DeliveryAddressForm d", DeliveryAddressForm.class)
                .getResultList();
    }

}
