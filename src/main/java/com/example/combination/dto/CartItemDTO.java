package com.example.combination.dto;


import lombok.*;


@Getter
@AllArgsConstructor
public class CartItemDTO { //최종 주문상품 변경사항 스냅샷 DTO

    private Long Id;
    private String skuId;
    private String skuName;
    private int quantity;
    private int unitPrice; //주문 당시 가격
    private int totalPrice;


}

