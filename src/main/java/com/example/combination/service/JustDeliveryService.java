package com.example.combination.service;

import com.example.combination.domain.delivery.JustDelivery;
import com.example.combination.domain.delivery.JustDeliveryStatus;
import com.example.combination.exception.DeliveryNotFoundException;
import com.example.combination.exception.InvalidDeliveryAddressException;
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
    public void updateJustDeliveryStatus(Long id, JustDeliveryStatus newStatus) {
        JustDelivery justDelivery = justDeliveryRepository.findByJustId(id)
                .orElseThrow(()-> new DeliveryNotFoundException(id));
        justDelivery.changeJustDeliveryStatus(newStatus);
    }
    
    /**
     * 배송 주소 검증
     */
    public void validateDeliveryAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new InvalidDeliveryAddressException(address, "배송 주소가 비어있습니다.");
        }
        
        // 배송 불가 지역 검증 (예시)
        String[] restrictedAreas = {"제주도", "울릉도", "독도"};
        for (String restrictedArea : restrictedAreas) {
            if (address.contains(restrictedArea)) {
                throw new InvalidDeliveryAddressException(address, "해당 지역은 배송이 불가능합니다: " + restrictedArea);
            }
        }
        
        // 주소 형식 검증 (간단한 예시)
        if (address.length() < 10) {
            throw new InvalidDeliveryAddressException(address, "주소가 너무 짧습니다. 상세 주소를 입력해주세요.");
        }
    }
}
//상태 변경 READY(배송 준비 중: 상품 패킹),SHIPPED(출고됨(운송장 번호 생성)),IN_TRANSIT(배송 중),DELIVERED(배송 완료),RETURNED(반송됨)

