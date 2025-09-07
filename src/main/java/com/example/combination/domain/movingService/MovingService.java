package com.example.combination.domain.movingService;

import com.example.combination.domain.order.Order;
import com.example.combination.domain.valuetype.HomeAddress;
import com.example.combination.domain.valuetype.MovingServiceAddress;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "moving_service")
public class MovingService {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(length = 500)
    private String movingServiceDescription;

    private String name; //회원 이름

    private String phoneNum; //회원 전화번호

    @Embedded
    private HomeAddress homeAddress; //기존 집 주소

    @Embedded
    private MovingServiceAddress movingServiceAddress; //새 주소 (배송지)

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

    @OneToOne(mappedBy = "movingService", fetch = FetchType.LAZY)
    private Order order;

    public void setOrder(Order order) {
        this.order = order;
        if (order.getMovingService() != null) {
            order.setMovingService(this);
        }
    }

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

    public int calculateMovingServicePrice() {
    }
}
