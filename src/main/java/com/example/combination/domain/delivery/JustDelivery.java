package com.example.combination.domain.delivery;

import com.example.combination.domain.order.Order;
import com.example.combination.domain.valuetype.DeliveryAddress;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "just_delivery")
public class JustDelivery {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name; //회원 이름

    private String phoneNum; //가입 정보 - 전화번호

    @Embedded
    private DeliveryAddress deliveryAddress;

    @Column(length = 500)
    private String deliveryDescription; //배송 요청사항 (DTO 로 받아옴)

    @Enumerated(EnumType.STRING)
    private JustDeliveryStatus justDeliveryStatus; //READY(배송 준비 중: 상품 패킹),SHIPPED(출고됨(운송장 번호 생성)),IN_TRANSIT(배송 중),DELIVERED(배송 완료),RETURNED(반송됨)

    private LocalDateTime deliveryTimeReady;

    private LocalDateTime deliveryTimeShipped;

    private LocalDateTime deliveryTimeInTransit;

    private LocalDateTime deliveryTimeDelivered;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;



    public void changeJustDeliveryStatus(JustDeliveryStatus newStatus) {
        this.justDeliveryStatus = newStatus;
        switch (newStatus) {
            case READY -> this.deliveryTimeReady = LocalDateTime.now();
            case SHIPPED -> this.deliveryTimeShipped = LocalDateTime.now();
            case IN_TRANSIT -> this.deliveryTimeInTransit = LocalDateTime.now();
            case DELIVERED -> this.deliveryTimeDelivered = LocalDateTime.now();
        }
    }

    //연관관계 편의 메서드  : Order <-> DeliveryAddressForm 동기화
    public void setOrder(Order order) {
        this.order = order;
        if (order.getJustDelivery() != null) {
            order.setJustDelivery(this);
        }
    }

    public int calculateDeliveryPrice() {  //추가 배송비
        return 0;
    }
}
