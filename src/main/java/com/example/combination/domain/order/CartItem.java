package com.example.combination.domain.order;


import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "CartItem")
public class CartItem {

    @Id @GeneratedValue
    private Long id;

    private String itemId; //상품 아이디)
    private int count; //수량 증감
    private int orderQuantity; //주문 상품별 수량
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;


    public void addCount(int count){
        this.count += count;
    }

    public void minusCount(int count){
        this.count -= count;
    }


    public CartItem createCartItem(ShoppingCart shoppingCart, OrderItem orderItem,int orderQuantity,int price) {
        return CartItem.builder()
                .shoppingCart(shoppingCart)
                .orderQuantity(orderQuantity)
                .build();
    }

}
