package com.example.combination.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@DiscriminatorValue("Table")
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Table extends Item {

    private String size;

}
