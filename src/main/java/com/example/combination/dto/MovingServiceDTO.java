package com.example.combination.dto;

import com.example.combination.domain.valuetype.HomeAddress;
import com.example.combination.domain.valuetype.MovingServiceAddress;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovingServiceDTO {

    private HomeAddress homeAddress;

    private MovingServiceAddress movingServiceAddress;

    private String requestDescription; //배송 요청사항
}
