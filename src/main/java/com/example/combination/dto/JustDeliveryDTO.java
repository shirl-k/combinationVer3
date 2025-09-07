package com.example.combination.dto;

import com.example.combination.domain.valuetype.DeliveryAddress;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JustDeliveryDTO{

    @NotNull
    @Valid
    private DeliveryAddress deliveryAddress;

    private String deliveryDescription;
}
