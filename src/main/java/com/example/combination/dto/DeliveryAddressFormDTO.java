package com.example.combination.dto;

import com.example.combination.domain.valuetype.DeliveryAddress;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryAddressFormDTO {

    private final DeliveryAddress deliveryAddress;

    private final String deliveryDescription;

}
