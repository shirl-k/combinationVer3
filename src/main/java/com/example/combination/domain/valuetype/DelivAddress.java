package com.example.combination.domain.valuetype;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "delivAddress1")
public class DelivAddress {

    @Id @GeneratedValue
    private Long id;

    private String city_d1;
    private String street_d1;
    private String zipcode_d1;

}
