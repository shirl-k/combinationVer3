package com.example.combination.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@DiscriminatorValue("Tiles")
@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tiles extends Item {

    private int size;
}
