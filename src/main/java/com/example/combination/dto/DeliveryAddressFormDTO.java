package com.example.combination.dto;

import com.example.combination.domain.valuetype.DelivAddress;
import com.example.combination.domain.valuetype.HomeAddress;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryAddressFormDTO {

    private final HomeAddress homeAddress;

    private final DelivAddress delivAddress;


}
