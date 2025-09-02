package com.example.combination.domain.item;

import com.example.combination.domain.category.CategoryItem;
import com.example.combination.domain.order.CartItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DiscriminatorValue("sku")
public class SKU extends Item { //재고 관리 단위

    @Column(unique = true, nullable = false, length = 50)
    private String skuId; //재고 관리 단위 상품 고유 코드

    private String skuName;

    private int stockQuantity;

    private int unitPrice; // 단가 (basePrice에 옵션 적용 후 실제 판매 가격)

    private String color;

    private String size;

    @OneToMany(mappedBy = "sku", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();


    // 상품 재고 관리
    public void decreaseStockQuantity(int quantity) {
        if(this.stockQuantity - quantity <0) {
            throw new IllegalArgumentException("재고 부족: 요청 수량 =" + quantity);
        }
        this.stockQuantity -= quantity;
    }

    public void increaseStockQuantity(int quantity) {
        this.stockQuantity += quantity;
    }

}
