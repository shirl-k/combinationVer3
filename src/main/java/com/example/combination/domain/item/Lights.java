package com.example.combination.domain.item;

import jakarta.persistence.*;
import lombok.*;

@DiscriminatorValue("Lights")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Lights extends Item {

    private String type;

}
