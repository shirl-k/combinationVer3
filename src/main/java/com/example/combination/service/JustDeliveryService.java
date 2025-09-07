package com.example.combination.service;

import com.example.combination.domain.delivery.JustDelivery;
import com.example.combination.domain.delivery.JustDeliveryStatus;
import com.example.combination.repository.JustDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JustDeliveryService {

    private final JustDeliveryRepository justDeliveryRepository;

    //변경 감지
    @Transactional
    public void updateJustDeliveryStatus(Long id,JustDeliveryStatus newStatus) {
        JustDelivery justDelivery = justDeliveryRepository.findByJustId(Long id) {

        }
    }

//상태 변경 READY(배송 준비 중: 상품 패킹),SHIPPED(출고됨(운송장 번호 생성)),IN_TRANSIT(배송 중),DELIVERED(배송 완료),RETURNED(반송됨)
}

// //변경 감지
//    @Transactional
//    public void updateDeliveryStatus(Long deliveryId, DeliveryStatus newStatus) {
//        DeliveryForm deliveryForm = justDeliveryRepository.findByDeliveryId(deliveryId)
//                .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
//        deliveryForm.changeDeliveryStatus(newStatus);
//    }
//}