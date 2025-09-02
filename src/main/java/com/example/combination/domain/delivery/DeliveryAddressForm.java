package com.example.combination.domain.delivery;

import com.example.combination.domain.order.Order;
import com.example.combination.domain.valuetype.DelivAddress;
import com.example.combination.domain.valuetype.HomeAddress;
import com.example.combination.dto.DeliveryAddressFormDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DeliveryAddressForm {

    @Id @GeneratedValue
    private long id;

    @Embedded
    private HomeAddress homeAddress;

    @Embedded
    @Column(nullable = false)
    private DelivAddress delivAddress;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Order order;

    public DeliveryAddressForm fromDeliveryAddressFormDTO(DeliveryAddressFormDTO deliveryAddressFormDTO) {
        return DeliveryAddressForm.builder()
                .homeAddress(deliveryAddressFormDTO.getHomeAddress())
                .delivAddress(deliveryAddressFormDTO.getDelivAddress())
                .build();

    }
}
