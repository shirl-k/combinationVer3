package com.example.combination.domain.payment;

import com.example.combination.domain.order.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payment")
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private LocalDateTime paidDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
//    private TotalPrice totalPrice;
//    private  discountPrice;


//    private PricePolicy pricePolicy;
//    private MembershipPolicy membershipPolicy;

}
