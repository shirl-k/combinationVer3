package com.example.combination.Assembler;

import com.example.combination.domain.order.CartItem;
import com.example.combination.dto.CartItemDTO;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
public class CartItemAssembler { //도메인 -> DTO 변환 책임
    public CartItemDTO toDTO(CartItem item) {
        return new CartItemDTO(
                item.getId(),
                item.getSkuId(),
                item.getItemName(),
                item.getSize(),
                item.getColor(),
                item.getTotalPrice(),
                item.getUnitPrice(),
                item.getQuantity()
        );
    }
    //CartItemDTO들의 List
        public List<CartItemDTO> toDTOs(List<CartItem> items) {
            return items.stream()
                    .map(this::toDTO) //toDTO 참조
                    .toList();
        }
    }

