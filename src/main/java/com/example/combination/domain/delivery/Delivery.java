package com.example.combination.domain.delivery;

import com.example.combination.domain.order.Order;
import com.example.combination.domain.valuetype.DelivAddress;
import com.example.combination.domain.valuetype.HomeAddress;
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

    private Order order;

    private HomeAddress homeAddress;

    private DelivAddress deliverAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private LocalDateTime deliveryTime;

}
