package com.example.combination.dto;

import com.example.combination.domain.valuetype.DelivAddress;
import com.example.combination.domain.valuetype.HomeAddress;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeliveryAddressFormDTO {

    private HomeAddress homeAddress;

    @Column(nullable = false)
    private DelivAddress delivAddress;

}
