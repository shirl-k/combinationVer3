package com.example.combination.domain.delivery;

import com.example.combination.domain.movingService.MovingServiceStatus;
import com.example.combination.domain.valuetype.HomeAddress;
import com.example.combination.domain.valuetype.MovingServiceAddress;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("movingService")
public class MovingService extends ServiceDetail{

    private String name;

    private String phoneNum;

    @Embedded
    private HomeAddress homeAddress;

    @Embedded
    private MovingServiceAddress movingServiceAddress;

    //이사 서비스
    @Enumerated(EnumType.STRING)
    private MovingServiceStatus movingServiceStatus;

    private LocalDateTime ready;

    private LocalDateTime shipped;

    private LocalDateTime inTransit;

    private LocalDateTime delivered;

    private LocalDateTime newSetting;

    private LocalDateTime home;

    private LocalDateTime transfer;

    private LocalDateTime newHome;

}
