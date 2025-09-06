package com.example.combination.domain.delivery;

import com.example.combination.domain.order.Order;
import com.example.combination.domain.valuetype.DeliveryAddress;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "delivery_form")
public class DeliveryForm{ //배송지 주소 엔티티에 Order 추가

    @Id
    @GeneratedValue
    private Long deliveryId; //배송건 Id

    @Embedded
    private DeliveryAddress deliveryAddress;

    private String name; //회원 이름

    private String phoneNum; //가입 정보 - 전화번호

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus; //READY(배송 준비 중: 상품 패킹),SHIPPED(출고됨(운송장 번호 생성)),IN_TRANSIT(배송 중),DELIVERED(배송 완료),RETURNED(반송됨)

    private LocalDateTime deliveryTimeReady;

    private LocalDateTime deliveryTimeShipped;

    private LocalDateTime deliveryTimeInTransit;

    private LocalDateTime deliveryTimeDelivered;


    @OneToOne(mappedBy = "deliveryForm", fetch = FetchType.LAZY)
    private Order order;

    @Column(length = 500)
    private String deliveryDescription;

    public void setOrder(Order order) {
        this.order = order;
        if(order.getDeliveryForm() != null) {
            order.setDeliveryForm(this);
        }
    }

    //연관관계 편의 메서드  : Order <-> DeliveryAddressForm 동기화


    //배송 추적 상태 업데이트
    public void changeDeliveryStatus(DeliveryStatus newStatus) {
        this.deliveryStatus = newStatus;
        switch (newStatus){
            case READY: this.deliveryTimeReady=LocalDateTime.now();
            case SHIPPED: this.deliveryTimeShipped=LocalDateTime.now();
            case IN_TRANSIT: this.deliveryTimeInTransit=LocalDateTime.now();
            case DELIVERED: this.deliveryTimeDelivered=LocalDateTime.now();
        }
    }
}

    //Assembler 에서 id랑 order 없이 주소만 받는 생성자
//    public DeliveryAddressForm(DeliveryAddress deliveryAddress) {
//        this.deliveryAddress = deliveryAddress;
//    }









