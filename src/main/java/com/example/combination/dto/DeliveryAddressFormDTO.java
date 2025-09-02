package com.example.combination.dto;

import com.example.combination.domain.order.Order;
import com.example.combination.domain.valuetype.DelivAddress;
import com.example.combination.domain.valuetype.HomeAddress;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeliveryAddressFormDTO {

    private HomeAddress homeAddress;

    private DelivAddress delivAddress;


}
