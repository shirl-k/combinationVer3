package com.example.combination.dto;

import com.example.combination.domain.valuetype.DeliveryAddress;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JustDeliveryDTO{
    
    private DeliveryAddress deliveryAddress; //배송지 주소

    private String requestDescription; // 배송 요청사항
}
