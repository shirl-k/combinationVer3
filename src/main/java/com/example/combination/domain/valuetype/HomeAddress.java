package com.example.combination.domain.valuetype;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HomeAddress {

//    @Id @GeneratedValue
//    private Long id;

    private String city;    //시
    private String district; //구
    private String roadNameAddress; //도로명 주소
    private String zipcode; //우편번호
}
