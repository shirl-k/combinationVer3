package com.example.combination.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@DiscriminatorValue("Curtain")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Curtain extends Item {
    private int size;
    private int texture;
}
