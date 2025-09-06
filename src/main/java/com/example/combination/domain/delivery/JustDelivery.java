package com.example.combination.domain.delivery;

import com.example.combination.domain.valuetype.DeliveryAddress;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("justOrder")
public class JustDelivery extends ServiceDetail{

    private String name; //회원 이름

    private String phoneNum; //가입 정보 - 전화번호

    @Embedded
    private DeliveryAddress deliveryAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus; //READY(배송 준비 중: 상품 패킹),SHIPPED(출고됨(운송장 번호 생성)),IN_TRANSIT(배송 중),DELIVERED(배송 완료),RETURNED(반송됨)

    private LocalDateTime deliveryTimeReady;

    private LocalDateTime deliveryTimeShipped;

    private LocalDateTime deliveryTimeInTransit;

    private LocalDateTime deliveryTimeDelivered;


}
