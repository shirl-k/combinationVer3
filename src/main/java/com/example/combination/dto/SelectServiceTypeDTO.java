package com.example.combination.dto;

import com.example.combination.domain.delivery.ServiceType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SelectServiceTypeDTO {

    private Long cartId;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;
}
