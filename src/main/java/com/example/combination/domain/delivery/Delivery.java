package com.example.combination.domain.delivery;

import com.example.combination.domain.order.Order;
import com.example.combination.dto.DeliveryAddressFormDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "delivery")
public class Delivery {

    @Id @GeneratedValue
    private Long id;





    @Enumerated(EnumType.STRING)           //물류 단계 추적
    private DeliveryStatus deliveryStatus;

    private LocalDateTime deliveryTime;


    //


    public static Delivery createDelivery(Order order, DeliveryAddressFormDTO deliveryAddressFormDTO, DeliveryStatus deliveryStatus) {
        return Delivery.builder()
                .build();
    }
}
