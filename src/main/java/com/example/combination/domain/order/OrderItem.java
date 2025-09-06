package com.example.combination.domain.order;


import com.example.combination.domain.item.SKU;
import com.example.combination.dto.CartItemDTO;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_item")
public class OrderItem { //결제 시 스냅샷(결제 당시 금액 영수증처럼 그대로 찍어내기. 가격 변동 고려)

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_id")
    private SKU sku;

    private String name;

    private String skuId;

    private int unitPrice; //주문 당시 단가.  //주문 당시 가격 (상품 가격 변동 고려)

    private int quantity; //주문 수량

    //private int calculatedTotalPrice;

    private int discountPrice;

    private int lineTotal; //unitPriceAtOrder * quantity

    private boolean selected = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Order order;

    private int movingServicePrice;

    //============핵심 비즈니스 로직 ==============//


    //CartItemDTO를 OrderItem엔티티로 변환 - 스냅샷 변환 팩토리
    public static OrderItem fromDTO(CartItemDTO cartItemDTO) {
        return OrderItem.builder()
                .skuId(cartItemDTO.getSkuId())
                .name(cartItemDTO.getSkuName())
                .quantity(cartItemDTO.getQuantity())
                .build();
    }


    public int getLineTotal() {

        return unitPrice * quantity;
    }

}



