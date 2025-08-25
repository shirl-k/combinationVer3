package com.example.combination.domain.order;

import com.example.combination.domain.business.PricePolicy;
import com.example.combination.domain.delivery.Delivery;
import com.example.combination.domain.member.Member;
import com.example.combination.domain.member.MembershipGrade;
import com.example.combination.domain.payment.Payment;
import com.example.combination.domain.payment.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "order")
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private MembershipGrade membershipGrade;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();



//    private DiscountPrice discountPrice;

    private PricePolicy pricePolicy;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private Delivery delivery;

    private LocalDateTime orderDate;

    private Payment payment;

    private PaymentMethod paymentMethod;

// totalPrice 주문서 총합금액
    int totalPrice = 0;

    for( OrderItem sum : orderItems ){
        totalPrice += sum.getTotalPrice();
    } return totalPrice;

}


