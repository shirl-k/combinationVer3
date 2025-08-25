package com.example.combination.domain.order;

import com.example.combination.domain.item.Item;
import com.example.combination.domain.price.TotalPrice;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private Order order;

    private int orderPrice; //주문 당시 가격 (상품 가격 변동 고려)

    private int orderQuantity; //주문 수량


    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private ShoppingCart shoppingCart;



//------------------//


    public OrderItem createOrderItem(
            Item item,
            int orderPrice,
            int orderQuantity
            ) {
        return OrderItem.builder()
                .item(item)
                .orderPrice(orderPrice)
                .orderQuantity(orderQuantity)
                .build();
    } //OrderItem 주문항목 객체 만듦

    public int getTotalPrice() {
        return orderQuantity * orderPrice;  //쇼핑카트에서 합산
    }

}
