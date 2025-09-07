package com.example.combination.dto;

import com.example.combination.domain.valuetype.DeliveryAddress;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JustDeliveryDTO{

    private DeliveryAddress deliveryAddress;

    private String deliveryDescription;
}
