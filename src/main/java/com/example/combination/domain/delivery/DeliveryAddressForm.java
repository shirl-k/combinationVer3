package com.example.combination.domain.delivery;

import com.example.combination.domain.valuetype.DelivAddress;
import com.example.combination.domain.valuetype.HomeAddress;
import com.example.combination.dto.DeliveryAddressFormDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity
public class DeliveryAddressForm {

    @Id @GeneratedValue
    private long id;


    private HomeAddress homeAddress;


    private DelivAddress delivAddress;
    
    //Assembler 에서 id랑 order 없이 주소만 받는 생성자
    public DeliveryAddressForm(HomeAddress homeAddress, DelivAddress delivAddress) {
        this.homeAddress = homeAddress;
        this.delivAddress = delivAddress;
    }
}
