package com.example.combination.Assembler;

import com.example.combination.domain.order.SelectedServiceType;
import com.example.combination.domain.order.ShoppingCart;
import com.example.combination.dto.SelectServiceTypeDTO;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SelectedServiceTypeAssembler {

    public SelectedServiceType fromdto(SelectServiceTypeDTO dto, ShoppingCart cart) {
        return SelectedServiceType.builder()
                .cartId(dto.getCartId())
                .serviceType(dto.getServiceType())
                .shoppingCartPrice(cart.calculateTotalPrice())
                .build();
    }
}
