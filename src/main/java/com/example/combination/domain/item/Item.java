package com.example.combination.domain.item;

import com.example.combination.domain.category.CategoryItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Entity
@Table(name = "item")
public abstract class Item {

    @Id @GeneratedValue
    private Long id;

    private String itemCode;

    private String name;

    private int price;

    private int stockQuantity;

    private List<CategoryItem> categoryItems = new ArrayList<>();

    private String itemDescription;


}

/*
 private Long id;

    private String itemCode;

    private String name;

    private int price;

    private int stockQuantity;

    private List<CategoryItem> categoryItems = new ArrayList<>();

    private String itemDescription;

 */