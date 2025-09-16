package com.example.combination.domain.delivery;

import com.example.combination.domain.common.OrderReference;
import com.example.combination.domain.order.Order;
import com.example.combination.domain.order.OrderStatus;
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
@Table(name = "just_deliveryies")
public class JustDelivery implements OrderReference {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name; //회원 이름

    private String phoneNum; //가입 정보 - 전화번호

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "just_city")),
            @AttributeOverride(name = "district", column = @Column(name = "just_district")),
            @AttributeOverride(name = "roadNameAddress", column = @Column(name = "just_road_name")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "just_zipcode")),
            @AttributeOverride(name = "detailAddress", column = @Column(name = "just_detail_address"))
    })
    private DeliveryAddress deliveryAddress;

    @Column(length = 500)
    private String deliveryDescription; //배송 요청사항 (DTO 로 받아옴)

    @Enumerated(EnumType.STRING)
    private JustDeliveryStatus justDeliveryStatus; //READY(배송 준비 중: 상품 패킹),SHIPPED(출고됨(운송장 번호 생성)),IN_TRANSIT(배송 중),DELIVERED(배송 완료),RETURNED(반송됨)

    private LocalDateTime deliveryTimeReady;

    private LocalDateTime deliveryTimeShipped;

    private LocalDateTime deliveryTimeInTransit;

    private LocalDateTime deliveryTimeDelivered;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private Order order;
    
    private Long orderCode; //주문 코드(주문 아이디)
    
    @Transient
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;



    public void changeJustDeliveryStatus(JustDeliveryStatus newStatus) {
        this.justDeliveryStatus = newStatus;
        switch (newStatus) {
            case READY -> this.deliveryTimeReady = LocalDateTime.now();
            case SHIPPED -> this.deliveryTimeShipped = LocalDateTime.now();
            case IN_TRANSIT -> this.deliveryTimeInTransit = LocalDateTime.now();
            case DELIVERED -> this.deliveryTimeDelivered = LocalDateTime.now();
        }
    }

    /**
     * OrderReference 인터페이스 구현
     */
    @Override
    public Long getOrderId() {
//        return orderId;
        return orderCode;
    }
    
    @Override
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
    
    /**
     * 주문과의 연관관계 설정 (순환참조 방지)
     */
    public void assignToOrder(Long orderId, OrderStatus orderStatus) { //Long orderId
        this.orderCode = orderId; //this.orderId = orderId
        this.orderStatus = orderStatus;
    }
    
    /**
     * 주문과의 연관관계 해제
     */
    public void detachFromOrder() {
        this.orderCode = null; //orderId
        this.orderStatus = null;
    }

    public int calculateDeliveryPrice() {  //추가 배송비
        return 0;
    }
}
