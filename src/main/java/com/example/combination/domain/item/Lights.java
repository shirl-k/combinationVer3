package com.example.combination.domain.item;

import jakarta.persistence.*;
import lombok.*;

@DiscriminatorValue("Lights")
@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lights extends Item {

    private String model;
    private String color;

}
