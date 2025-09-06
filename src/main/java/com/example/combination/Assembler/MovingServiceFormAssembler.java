package com.example.combination.Assembler;

import com.example.combination.domain.member.Member;
import com.example.combination.domain.movingService.MovingServiceForm;
import com.example.combination.domain.movingService.MovingServiceStatus;
import com.example.combination.domain.order.Order;
import com.example.combination.domain.valuetype.UserInfo;
import com.example.combination.dto.MovingServiceFormDTO;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MovingServiceFormAssembler {

    public MovingServiceForm fromDTO(MovingServiceFormDTO dto, UserInfo userInfo, Member member, Order order) {
        return MovingServiceForm.builder()
                .homeAddress(dto.getHomeAddress())
                .movingServiceAddress(dto.getMovingServiceAddress())
                .movingServiceDescription(dto.getMovingServiceDescription())
                .name(order.getMember().getName())
                .phoneNum(userInfo.getPhoneNum())
                .order(order)
                .movingServiceStatus(MovingServiceStatus.READY)
                .build();
    }
}
