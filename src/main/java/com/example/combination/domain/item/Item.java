package com.example.combination.domain.item;

import com.example.combination.domain.category.CategoryItem;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@Entity
@Table(name = "item")
public abstract class Item { //상품 카탈로그 SPU

    @Id @GeneratedValue
    private Long spuId;  // SPU Id

    private String name; //상품명

    private int basePrice;  //기본 가격 (옵션 가산 전)

    private String itemDescription; //상품 상세 설명

    @OneToMany
    private List<CategoryItem> categoryItems = new ArrayList<>();


    private int unitPrice;
    private int discountRate;
    private int discountAmount;
    //private int basePrice;
    //private Item item;

    //상품 전체 일괄 적용 - 이벤트 할인 금액 계산기
    public int discountAmount() {
        discountAmount = basePrice * discountRate/100;
        return discountAmount;
    }

    //단가 계산기
    public int calculateUnitPrice(int discountAmount) {
        unitPrice = basePrice - discountAmount;
        return unitPrice;
    }

}

