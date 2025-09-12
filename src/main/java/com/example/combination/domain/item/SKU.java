package com.example.combination.domain.item;

import com.example.combination.domain.order.CartItem;
import com.example.combination.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@DiscriminatorValue("sku")
public class SKU extends Item { //재고 관리 단위

    @Column(unique = true, nullable = false, length = 50)
    private String skuId; //재고 관리 단위 상품 고유 코드

    private String skuName;

    private int stockQuantity;
    
//    @Version
//    private Long version; // 낙관적 락을 위한 버전 필드

    private int unitPrice; // 단가 (basePrice에 옵션 적용 후 실제 판매 가격)

    private String color;

    private String size;

    @OneToMany(mappedBy = "sku", cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();


    // 상품 재고 관리
    public void decreaseStockQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다: " + quantity);
        }
        
        if (this.stockQuantity < quantity) {
            throw new OutOfStockException(this.skuId, quantity, this.stockQuantity);
        }
        
        this.stockQuantity -= quantity;
    }

    public void increaseStockQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다: " + quantity);
        }
        this.stockQuantity += quantity;
    }
    
    /**
     * 재고 복구 (주문 취소 시 사용)
     */
    public void restoreStockQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("복구 수량은 0보다 커야 합니다: " + quantity);
        }
        this.stockQuantity += quantity;
    }
    
    /**
     * 재고 확인
     */
    public boolean isInStock(int quantity) {
        return this.stockQuantity >= quantity;
    }
    
    /**
     * 품절 여부 확인
     */
    public boolean isOutOfStock() {
        return this.stockQuantity <= 0;
    }

}
