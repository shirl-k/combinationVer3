package com.example.combination.domain.valuetype;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "homeAddress")
public class HomeAddress {

    @Id @GeneratedValue
    private Long id;

    private String city;
    private String street;
    private String zipcode;
}
