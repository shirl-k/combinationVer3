package com.example.combination.dto;


import com.example.combination.Assembler.CartItemAssembler;
import com.example.combination.domain.item.SKU;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;


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

