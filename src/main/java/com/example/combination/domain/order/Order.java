package com.example.combination.domain.order;

import com.example.combination.domain.delivery.Delivery;
import com.example.combination.domain.delivery.DeliveryAddressForm;
import com.example.combination.domain.member.Member;
import com.example.combination.domain.member.MembershipGrade;
import com.example.combination.domain.payment.PaymentMethod;
import com.example.combination.domain.valuetype.DelivAddress;
import com.example.combination.domain.valuetype.HomeAddress;
import com.example.combination.dto.DeliveryAddressFormDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "orders_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private MembershipGrade membershipGrade;

    private int memberDiscount;


    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문 자체 흐름. 주문의 큰 그림만 관리 (결제가 실패했다고 해서 Order를 바로 실패로 두진 않음. 아직 살아있을 수 있음.)
                                        // CREATED(주문 생성됨. 결제 전), CONFIRMED(결제 성공, 주문 확정), CANCELLED(결제 전 취소), COMPLETED(배송까지 완료)

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;


    private LocalDate createOrderDate; //주문 생성 시점

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private DeliveryAddressForm deliveryAddressForm;

    //============핵심 비즈니스 로직==============//

    //연관관계 편의 메서드 //orderItem 과 Order 양쪽 동일하게 업데이트
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    //주문 취소 OrderStatus - CANCELLED

    //배송지 입력
    public void getDeliveryAddressForm(DeliveryAddressForm deliveryAddressForm) {
        this.deliveryAddressForm = deliveryAddressForm;
    }

    //결제 방식
    public void selectPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    //총 결제 금액
    public int calculateTotalPrice() {
        return orderItems.stream()
                .filter(OrderItem::isSelected)
                .mapToInt(OrderItem::getLineTotal) // unitPrice * quantity
                .sum()-memberDiscount;
    }

    //최종 주문 스냅샷
    public static Order createFinalOrder(Member member, List<OrderItem> orderItems, PaymentMethod paymentMethod,
                                         int finalPrice, int memberDiscount, OrderStatus orderStatus,DeliveryAddressForm deliveryAddressForm,
                                         int totalPrice, ShoppingCart cart)
    {
        return Order.builder()
                .member(member)
                .membershipGrade(member.getMembershipGrade())
                .orderStatus(OrderStatus.CREATED)
                .orderItems(orderItems)
                .deliveryAddressForm(deliveryAddressForm)
                .paymentMethod(paymentMethod)
                .memberDiscount(memberDiscount)  //discountRate
                .createOrderDate(LocalDate.now())
                .build();
    }

    //.calculatedTotalPrice(cart.calculateTotalPrice())

    //Order 변경감지
    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}

