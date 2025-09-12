package com.example.combination.Assembler;

import com.example.combination.domain.member.Member;
import com.example.combination.domain.movingService.MovingService;
import com.example.combination.domain.movingService.MovingServiceStatus;
import com.example.combination.domain.order.Order;
import com.example.combination.domain.valuetype.UserInfo;
import com.example.combination.dto.MovingServiceDTO;

public class MovingServiceAssembler {

    public MovingService fromDTO(MovingServiceDTO dto, UserInfo userInfo, Member member, Order order) {
        MovingService movingService = MovingService.builder()
                .name(order.getMember().getName())
                .phoneNum(order.getMember().getUserInfo().getPhoneNum())
                .homeAddress(dto.getHomeAddress())
                .movingServiceAddress(dto.getMovingServiceAddress())
                .movingServiceDescription(dto.getRequestDescription())
                .orderCode(order.getOrderId()) //주문 코드(주문 아이디)
                .movingServiceStatus(MovingServiceStatus.READY)
                .build();
        
        // 순환참조 방지를 위한 연관관계 설정
        movingService.assignToOrder(order.getOrderId(), order.getOrderStatus());
        
        return movingService;
    }
}
