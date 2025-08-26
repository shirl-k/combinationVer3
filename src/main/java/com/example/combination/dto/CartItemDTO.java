package com.example.combination.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    @Id
    @GeneratedValue
    private Long id;

    private int orderQuantity;
}
