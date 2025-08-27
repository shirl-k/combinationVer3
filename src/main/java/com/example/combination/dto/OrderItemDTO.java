package com.example.combination.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDTO {


    private String userId;

    private Long skuId; // 상품(SKU) ID
    private String itemName;
    private String size;
    private String color;
    private int quantity;
    private int unitPrice; //주문 당시 가격

    public int getTotalPrice() {
        return unitPrice*quantity;
    }
}
