package com.example.combination.Assembler;

import com.example.combination.domain.delivery.DeliveryForm;
import com.example.combination.domain.delivery.DeliveryStatus;
import com.example.combination.domain.member.Member;
import com.example.combination.domain.order.Order;
import com.example.combination.domain.valuetype.UserInfo;
import com.example.combination.dto.DeliveryAddressFormDTO;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Component
public class DeliveryFormAssembler {

      //DeliveryForm (DeliveryAddressForm DTO -> DeliveryForm 엔티티 변환)
        public DeliveryForm fromDTO(DeliveryAddressFormDTO dto, Member member, UserInfo userInfo, Order order) {
            return DeliveryForm.builder()
                    .deliveryAddress(dto.getDeliveryAddress())
                    .deliveryDescription(dto.getDeliveryDescription())
                    .name(member.getName())
                    .phoneNum(userInfo.getPhoneNum())
                    .deliveryStatus(DeliveryStatus.READY)
                    .deliveryTimeReady(LocalDateTime.now())
                    .order(order)
                    .build();
        }
    }


