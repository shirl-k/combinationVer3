package com.example.combination.domain.valuetype;

import jakarta.persistence.*;
import lombok.*;

@Embeddable //값타입 클래스는 반드시 불변설계로 만들 것. Setter대신 생성자로만 값 설정
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //NoArgsConstructor은 기본생성자가 Public 인데 JPA 베스트프랙티스는 기본생성자를 PROTECTED로 만드는 것
@AllArgsConstructor
@Builder
@Table(name = "deliv_address")
public class DelivAddress {

    @Id @GeneratedValue
    private Long id;

    private String city;
    private String street;
    private String zipcode;

}
