package com.example.combination.domain.order;

import com.example.combination.domain.member.Member;
import com.example.combination.domain.member.MembershipGrade;
import com.example.combination.domain.payment.PaymentMethod;
import com.example.combination.domain.valuetype.DelivAddress;
import com.example.combination.domain.valuetype.HomeAddress;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

//    private DiscountPrice discountPrice;

//    private PricePolicy pricePolicy;

    @Column(nullable = false)
    private DelivAddress delivAddress;

    private HomeAddress homeAddress;





}


