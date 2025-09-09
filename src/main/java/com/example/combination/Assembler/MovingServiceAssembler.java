package com.example.combination.Assembler;

import com.example.combination.domain.member.Member;
import com.example.combination.domain.movingService.MovingService;
import com.example.combination.domain.movingService.MovingServiceStatus;
import com.example.combination.domain.order.Order;
import com.example.combination.domain.valuetype.UserInfo;
import com.example.combination.dto.MovingServiceDTO;

public class MovingServiceAssembler {

    public MovingService fromDTO(MovingServiceDTO dto, UserInfo userInfo, Member member, Order order) {
        return MovingService.builder()
                .name(order.getMember().getName())
                .phoneNum(order.getMember().getUserInfo().getPhoneNum())
                .homeAddress(dto.getHomeAddress())
                .movingServiceAddress(dto.getMovingServiceAddress())
                .movingServiceDescription(dto.getRequestDescription())
                .order(order)
                .movingServiceStatus(MovingServiceStatus.READY)
                .build();
    }
}
