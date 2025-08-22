package com.example.combination.domain.order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@NoArgsConstructor//매개변수 없는 생성자
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "shoppingCart")
public class ShoppingCart {

    @Id @GeneratedValue
    private Long id;
    private OrderItem orderItem;
    private int orderprice;
    private int orderQuantity;
    private int totalPrice;
    private int discountPrice;
}
