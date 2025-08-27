package com.example.combination.dto;

import com.example.combination.domain.member.Member;
import com.example.combination.domain.order.CartItem;
import com.example.combination.domain.order.ShoppingCart;
import jakarta.persistence.*;
import lombok.*;


@Getter
@AllArgsConstructor
@Builder
public class CartItemDTO { //최종 주문상품 변경사항
    @Id
    @GeneratedValue
    private Long id;

    private String itemId;
    private String itemName;
    private String size;
    private String color;
    private int quantity;
    private int unitPrice; //주문 당시 가격
    private int totalPrice;

    public static CartItemDTO fromEntity(CartItem cartItem, ShoppingCart shoppingCart) {
        return CartItemDTO.builder()
                .itemId(cartItem.getSkuId())
                .itemName(cartItem.getItemName())
                .size(cartItem.getSize())
                .color(cartItem.getColor())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice())
                .totalPrice(cartItem.getTotalPrice())
                .build();
    }
}
