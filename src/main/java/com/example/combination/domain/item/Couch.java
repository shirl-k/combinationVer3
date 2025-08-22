package com.example.combination.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@DiscriminatorValue("Couch")
@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Couch extends Item {

    private int size;
    private int texture;
}
