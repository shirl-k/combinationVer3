package com.example.combination.domain.order;

import com.example.combination.domain.item.Item;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "orderItem")
public class OrderItem {

    @Id @GeneratedValue
    private Long id;

    private Item item;

    private Order order;

    private int orderPrice; //주문 당시 가격 (상품 가격 변동 고려)

    private int orderQuantity; //주문 수량 (count)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private ShoppingCart shoppingCart;

}

