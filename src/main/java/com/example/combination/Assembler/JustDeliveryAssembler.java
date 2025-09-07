package com.example.combination.Assembler;

import com.example.combination.domain.delivery.JustDelivery;
import com.example.combination.domain.delivery.JustDeliveryStatus;
import com.example.combination.domain.member.Member;
import com.example.combination.domain.movingService.MovingService;
import com.example.combination.domain.movingService.MovingServiceStatus;
import com.example.combination.domain.order.Order;
import com.example.combination.domain.order.ShoppingCart;
import com.example.combination.domain.valuetype.UserInfo;
import com.example.combination.dto.JustDeliveryDTO;
import com.example.combination.dto.MovingServiceDTO;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JustDeliveryAssembler {

    //JustDelivery (JustDeliveryDTO -> JustDelivery 엔티티 변환)
    public JustDelivery fromDTO(JustDeliveryDTO dto, Member member, UserInfo userInfo, Order order) {
        return JustDelivery.builder()
                .name(member.getName())
                .phoneNum(order.getMember().getUserInfo().getPhoneNum())
                .deliveryAddress(dto.getDeliveryAddress())
                .deliveryDescription(dto.getDeliveryDescription())
                .order(order)
                .justDeliveryStatus(JustDeliveryStatus.READY)
                .build();
    }
}

