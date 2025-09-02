package com.example.combination.Assembler;

import com.example.combination.domain.delivery.DeliveryAddressForm;
import com.example.combination.domain.order.CartItem;
import com.example.combination.dto.DeliveryAddressFormDTO;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class DeliveryAddressFormAssembler {

    public DeliveryAddressForm fromDTO(DeliveryAddressFormDTO dto) {
        return new DeliveryAddressForm(
                dto.getHomeAddress(),
                dto.getDelivAddress()
        );
    }
}
