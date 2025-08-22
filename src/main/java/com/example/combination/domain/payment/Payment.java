package com.example.combination.domain.payment;

import com.example.combination.domain.business.MembershipPolicy;
import com.example.combination.domain.business.PricePolicy;
import com.example.combination.domain.order.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "payment")
public class Payment {
    private Long id;
    private PayStatus payStatus;

    private Order order;
    private LocalDateTime orderDate;
    private int totalPrice;
    private int discountPrice;
    private PaymentMethod paymentMethod;
    private PricePolicy pricePolicy;
    private MembershipPolicy membershipPolicy;

}
