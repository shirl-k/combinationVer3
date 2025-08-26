package com.example.combination.domain.order;

import com.example.combination.domain.item.Item;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.ui.Model;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orderItem")
public class OrderItem {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private Order order;

    private int orderPrice; //주문 당시 가격 (상품 가격 변동 고려)

    private int orderQuantity; //주문 수량




//
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private ShoppingCart shoppingCart;


    //------------------//
    public OrderItem createOrderItem(Item item, int orderQuantity, int orderPrice) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderQuantity(orderQuantity);
        orderItem.setOrderPrice(orderPrice);
        return orderItem;
    } //CartItemDTO로 이동

    public void addOrderQuantity(int orderQuantity) {
        this.orderQuantity += orderQuantity;
    } //수량 증감

    public void minusOrderQuantity(int orderQuantity) {
        this.orderQuantity -= orderQuantity;
    }

//    public Order getOrder(Item item, int orderQuantity, int orderPrice) {
//        return Order.builder()
//                .item(item)
//                .orderQuantity(orderQuantity)
//                .orderPrice(orderPrice)
//                .build();
//
//    }
    //단건 결제

}

//
//    public OrderItem createOrderItem(
//            Item item,
//            int orderPrice,
//            int orderQuantity
//            ) {
//        return OrderItem.builder()
//                .item(item)
//                .orderPrice(orderPrice)
//                .orderQuantity(orderQuantity)
//                .build();
    //OrderItem 주문항목 객체 만듦





