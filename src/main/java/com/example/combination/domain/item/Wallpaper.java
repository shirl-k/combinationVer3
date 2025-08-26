package com.example.combination.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@DiscriminatorValue("Wallpaper")
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallpaper extends Item {

    private String texture;

}
