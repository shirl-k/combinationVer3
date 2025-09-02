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
    private DeliveryStatus deliveryStatus; //READY(배송 준비 중: 상품 패킹),SHIPPED(출고됨(운송장 번호 생성)),IN_TRANSIT(배송 중),DELIVERED(배송 완료),RETURNED(반송됨)

    private LocalDateTime deliveryTime;


    //
    public static Delivery createDelivery(Order order, DeliveryAddressFormDTO deliveryAddressFormDTO, DeliveryStatus deliveryStatus) {
        return Delivery.builder()
                .build();
    }
}
