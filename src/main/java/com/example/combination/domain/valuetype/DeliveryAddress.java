package com.example.combination.domain.valuetype;

import jakarta.persistence.*;
import lombok.*;

@Embeddable //값타입 클래스는 반드시 불변설계로 만들 것. Setter대신 생성자로만 값 설정
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) //NoArgsConstructor은 기본생성자가 Public 인데 JPA 베스트프랙티스는 기본생성자를 PROTECTED로 만드는 것
@AllArgsConstructor
public class DeliveryAddress {

//    @Id @GeneratedValue
//    private Long id;

    private String city;    //시
    private String district; //구
    private String roadNameAddress; //도로명 주소
    private String zipcode; //우편번호
    private String detailAddress; //상세 주소 (동/호수, 건물명 등)

}
