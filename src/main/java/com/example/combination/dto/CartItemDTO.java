package com.example.combination.dto;


import lombok.*;


@Getter
@AllArgsConstructor
public class CartItemDTO { //최종 주문상품 변경사항

    private Long id;
    private Long skuId; //선택된 아이템 아이디
    private String itemName;
    private String size;
    private String color;
    private int quantity;
    private int unitPrice; //주문 당시 가격
    private int totalPrice;


//    public static CartItemDTO fromEntity(CartItem cartItem, ShoppingCart shoppingCart) {
//        return CartItemDTO.builder()
//                .itemId(cartItem.getSkuId())
//                .itemName(cartItem.getItemName())
//                .size(cartItem.getSize())
//                .color(cartItem.getColor())
//                .quantity(cartItem.getQuantity())
//                .unitPrice(cartItem.getUnitPrice())
//                .totalPrice(cartItem.getTotalPrice())
//                .build();
//    }
}
