package com.example.combination.Assembler;

import com.example.combination.domain.order.CartItem;
import com.example.combination.domain.order.ShoppingCart;
import com.example.combination.dto.CartItemDTO;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
public class CartItemAssembler { //Assembler: 도메인 -> DTO 변환 책임  
    
    //CartItem 도메인 -> CartItemDTO 변환
    public CartItemDTO toDTO(CartItem item) {
        return new CartItemDTO(
                item.getId(),
                item.getSku().getSkuId(),
                item.getSku().getSkuName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice()
        );
    }
    //List<CartItem> -> List<CartItemDTO> 변환
    public List<CartItemDTO> toDTOs(List<CartItem> cartItems) {//CartItem 리스트를 CartItemDTO 리스트로 변환
        return cartItems.stream()
                .map(this::toDTO) // 위에 정의한 toDTO() 재사용
                .toList();
    }
}
/*
    private Long Id;
    private String skuId;
    private String skuName;
    private int quantity;
    private int unitPrice; //주문 당시 가격
    private int totalPrice;

 */