package com.example.combination.dto;

import com.example.combination.domain.valuetype.HomeAddress;
import com.example.combination.domain.valuetype.MovingServiceAddress;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MovingServiceFormDTO {

    private final HomeAddress homeAddress;

    private final MovingServiceAddress movingServiceAddress;

    private final String movingServiceDescription;
}
