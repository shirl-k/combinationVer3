package com.example.combination.domain.order;

import com.example.combination.domain.member.Member;
import com.example.combination.domain.member.MembershipGrade;
import com.example.combination.domain.payment.PaymentMethod;
import com.example.combination.domain.valuetype.DelivAddress;
import com.example.combination.domain.valuetype.HomeAddress;
import com.example.combination.domain.valuetype.UserInfo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //Order -> Payments 사이 단계

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private int memberDiscount;    //discountPrice  ->  DiscountPrice discountPrice

    private int totalPrice; //총합 금액


    private LocalDate createOrderDate; //주문 생성 시점
    @Column(nullable = false)
    @Embedded
    private DelivAddress delivAddress;

    @Embedded
    private HomeAddress homeAddress;

    private int finalPrice;

    //============핵심 비즈니스 로직==============//

    //연관관계 편의 메서드 //orderItem 과 Order 양쪽 동일하게 업데이트
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void calculateFinalPrice() {
        int orderItemSum = orderItems.stream()
                .mapToInt(OrderItem::getLineTotal)
                .sum();
        this.finalPrice = orderItemSum - memberDiscount;
    }

    //최종 주문 스냅샷
    public static Order createFinalOrder(Member member, List<OrderItem> orderItems, PaymentMethod paymentMethod,
                                         DelivAddress delivAddress, int finalPrice, int memberDiscount, OrderStatus orderStatus,
                                         int totalPrice)
    {
        return Order.builder()
                .member(member)
                .orderStatus(OrderStatus.CREATED)
                .membershipGrade(member.getMembershipGrade())
                .orderItems(orderItems)
                .delivAddress(delivAddress)
                .homeAddress(member.getHomeAddress())
                .paymentMethod(paymentMethod)
                .totalPrice(totalPrice)
                .memberDiscount(memberDiscount)
                .finalPrice(finalPrice)
                .createOrderDate(LocalDate.now())
                .build();
    }
}

