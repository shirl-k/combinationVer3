package com.example.combination.dto;


import com.example.combination.domain.payment.PaymentMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaymentDataDTO {

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private int memberDiscount;

}
