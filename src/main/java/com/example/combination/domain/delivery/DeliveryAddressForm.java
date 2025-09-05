package com.example.combination.domain.delivery;

import com.example.combination.Assembler.DeliveryAddressFormAssembler;
import com.example.combination.domain.member.Member;
import com.example.combination.domain.order.Order;
import com.example.combination.domain.valuetype.DelivAddress;
import com.example.combination.domain.valuetype.HomeAddress;
import com.example.combination.domain.valuetype.UserInfo;
import com.example.combination.dto.DeliveryAddressFormDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "delivery_address_form")
public class DeliveryAddressForm { //배송지 주소 엔티티에 Order 추가

    @Id @GeneratedValue
    private Long deliveryId; //배송건 Id

    @Embedded
    private HomeAddress homeAddress;
    @Embedded
    private DelivAddress delivAddress;

    private String name; //회원 이름

    private String phoneNum; //가입 정보 - 전화번호

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus; //READY(배송 준비 중: 상품 패킹),SHIPPED(출고됨(운송장 번호 생성)),IN_TRANSIT(배송 중),DELIVERED(배송 완료),RETURNED(반송됨)

    private LocalDateTime deliveryTimeReady;

    private LocalDateTime deliveryTimeShipped;

    private LocalDateTime deliveryTimeInTransit;

    private LocalDateTime deliveryTimeDelivered;

    private LocalDateTime deliveryTimeReturned;

//    private LocalDateTime deliveryTimeUpdated;

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;
    
    //Assembler 에서 id랑 order 없이 주소만 받는 생성자
    public DeliveryAddressForm (HomeAddress homeAddress, DelivAddress delivAddress) {
        this.homeAddress = homeAddress;
        this.delivAddress = delivAddress;
    }

    //배송 추적 상태 업데이트
    public void changeDeliveryStatus(DeliveryStatus newStatus) {
        this.deliveryStatus = newStatus;
    }

    //주소지 + 배달 패키징
    public static DeliveryAddressForm createDelivery(DeliveryAddressForm deliveryAddressForm,
                                              Order order, UserInfo userInfo, Member member) {
        return DeliveryAddressForm.builder()
                .homeAddress(deliveryAddressForm.getHomeAddress())
                .delivAddress(deliveryAddressForm.getDelivAddress())
                .name(member.getName())
                .phoneNum(userInfo.getPhoneNum())
                .deliveryStatus(DeliveryStatus.READY)
                .deliveryTimeReady(LocalDateTime.now())
                .order(order)
                .build();
    }
} 
