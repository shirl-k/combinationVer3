package com.example.combination.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectOrderRequestDTO {
    private Long spuId;
    private String itemName;
    private String skuId;
    private int quantity;
    private int unitPrice;
}
