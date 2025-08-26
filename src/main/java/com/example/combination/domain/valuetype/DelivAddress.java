package com.example.combination.domain.valuetype;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "delivAddress")
public class DelivAddress {

    @Id @GeneratedValue
    private Long id;

    private String city;
    private String street;
    private String zipcode;

}
