package com.example.combination.Assembler;

import com.example.combination.domain.delivery.JustDelivery;
import com.example.combination.domain.delivery.JustDeliveryStatus;
import com.example.combination.domain.member.Member;
import com.example.combination.domain.order.Order;
import com.example.combination.domain.valuetype.UserInfo;
import com.example.combination.dto.JustDeliveryDTO;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JustDeliveryAssembler {
    //JustDelivery (JustDeliveryDTO -> JustDelivery 엔티티 변환)
    public JustDelivery fromDTO(JustDeliveryDTO dto, Member member, UserInfo userInfo, Order order) {
        JustDelivery justDelivery = JustDelivery.builder()
                .name(member.getName())
                .phoneNum(order.getMember().getUserInfo().getPhoneNum())
                .deliveryAddress(dto.getDeliveryAddress())
                .deliveryDescription(dto.getRequestDescription())
                .orderCode(order.getOrderId()) //주문 아이디(주문 코드)
                .justDeliveryStatus(JustDeliveryStatus.READY)
                .build();
        
        // 순환참조 방지를 위한 연관관계 설정
        justDelivery.assignToOrder(order.getOrderId(), order.getOrderStatus());
        
        return justDelivery;
    }
}

