package com.example.combination.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartItemRequestDTO {

    private Long spuId; //SPU Id
    private String itemName; // 상품명
    private String skuId;
    private int quantity; //주문 수량
    private int unitPrice; //주문 당시 가격


}
