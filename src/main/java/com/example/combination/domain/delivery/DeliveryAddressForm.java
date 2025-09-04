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
@NoArgsConstructor
@Entity
public class DeliveryAddressForm { //배송지 주소 엔티티에 Order 추가

    @Id @GeneratedValue
    private long id;

    private HomeAddress homeAddress;

    private DelivAddress delivAddress;

    private String name;

    private String phoneNum;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus; //READY(배송 준비 중: 상품 패킹),SHIPPED(출고됨(운송장 번호 생성)),IN_TRANSIT(배송 중),DELIVERED(배송 완료),RETURNED(반송됨)

    private LocalDateTime deliveryTime;

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;
    
    //Assembler 에서 id랑 order 없이 주소만 받는 생성자
    public DeliveryAddressForm (HomeAddress homeAddress, DelivAddress delivAddress) {
        this.homeAddress = homeAddress;
        this.delivAddress = delivAddress;
    }
    //주소지 + 배달 패키징
    public DeliveryAddressForm createDelivery(DeliveryAddressForm deliveryAddressForm, LocalDateTime deliveryTime,
                                              Order order, UserInfo userInfo, Member member) {
        return DeliveryAddressForm.builder()
                .homeAddress(deliveryAddressForm.getHomeAddress())
                .delivAddress(deliveryAddressForm.getDelivAddress())
                .name(member.getName())
                .phoneNum(userInfo.getPhoneNum())
                .deliveryStatus(DeliveryStatus.READY)
                .deliveryTime(LocalDateTime.now())
                .order(order)
                .build();
    }
} 
