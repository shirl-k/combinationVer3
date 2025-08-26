package com.example.combination.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {

    private Long id; //단일 결제

    private int orderQuantity; //단일 결제

    private List<CartItemDTO> cartItems; //장바구니 결제

}
