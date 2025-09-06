package com.example.combination.domain.movingService;

import com.example.combination.domain.delivery.DeliveryStatus;
import com.example.combination.domain.order.Order;
import com.example.combination.domain.valuetype.HomeAddress;
import com.example.combination.domain.valuetype.MovingServiceAddress;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "moving_service_form")
public class MovingServiceForm {

    @Id
    @GeneratedValue
    private Long id;

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

    @OneToOne(mappedBy = "movingServiceForm", fetch = FetchType.LAZY)
    private Order order;

    @Column(length = 500)
    private String movingServiceDescription;

    public void changeMovingServiceStatus(MovingServiceStatus newStatus) {
        switch (newStatus) {
            case READY:
                this.ready = LocalDateTime.now();
            case SHIPPED:
                this.shipped = LocalDateTime.now();
            case IN_TRANSIT:
                this.inTransit = LocalDateTime.now();
            case DELIVERED:
                this.delivered = LocalDateTime.now();
            case NEW_SETTING:
                this.newSetting = LocalDateTime.now();
            case HOME:
                this.home = LocalDateTime.now();
            case TRASFER:
                this.transfer = LocalDateTime.now();
            case NEWHOME:
                this.newHome = LocalDateTime.now();
        }
    }
}


