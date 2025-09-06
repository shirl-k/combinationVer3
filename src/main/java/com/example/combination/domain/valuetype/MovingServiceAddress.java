package com.example.combination.domain.valuetype;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MovingServiceAddress {

    @Id @GeneratedValue
    private Long id;

    private String city;    //시
    private String district; //구
    private String roadNameAddress; //도로명 주소
    private String zipcode; //우편번호

}
