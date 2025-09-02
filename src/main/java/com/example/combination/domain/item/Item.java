package com.example.combination.domain.item;

import com.example.combination.domain.category.CategoryItem;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Entity
@Table(name = "item")
public abstract class Item { //상품 카탈로그

    @Id @GeneratedValue
    private Long spuId;  // SPU Id

    private String name; //상품명

    private int basePrice;  //기본 가격 (옵션 가산 전)

    private String itemDescription; //상품 상세 설명

    @OneToMany
    private List<CategoryItem> categoryItems = new ArrayList<>();

}

