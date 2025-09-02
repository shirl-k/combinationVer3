package com.example.combination.domain.payment;

import com.example.combination.domain.order.Order;
import com.example.combination.dto.PaymentDataDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class PaymentData {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;


    private int finalPrice;

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;

    public static PaymentData fromPaymentDataDTO(PaymentDataDTO paymentDataDTO, Order order) {
        return PaymentData.builder()
                .paymentMethod(paymentDataDTO.getPaymentMethod())
                .finalPrice(order.calculateLineTotalPrice())
                .build();
    }

}
