package com.example.combination.domain.order;


import com.example.combination.dto.OrderItemDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue //jpa 기본은 auto.  mysql : (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;

    private String userId;
    private Long skuId; //상품 아이디 // SKU ID
    private String itemName;
    private String size;
    private String color;
    private int quantity; //주문 상품별 수량
    private int unitPrice; // 단가

    private boolean selected = true; // 장바구니에서 체크된 상품만 결제 적용하도록 갖고 있는 필드

    //OrderItemDTO -> CartItem 엔티티 변환, CartItem 객체 생성
    public static CartItem fromOrderItemDTO(OrderItemDTO dto, ShoppingCart shoppingCart) {
        return CartItem.builder()
                .shoppingCart(shoppingCart)
                .userId(dto.getUserId())
                .id(dto.getSkuId())
                .itemName(dto.getItemName())
                .size(dto.getSize())
                .color(dto.getColor())
                .quantity(dto.getQuantity()) //수량
                .unitPrice(dto.getUnitPrice())
                .build();
    }
    //DTO-> 엔티티 변환 책임은 static 팩토리 메서드로 분리


    //총합 금액
    public int getTotalPrice() {
        return unitPrice * quantity;

        /* private int totalPrice;처럼 변수로 고정하지 않는 이유는 ShoppingCart에서 실시간으로 가격이 변동되기 때문에
        계산 메서드로 두는 것이 낫다. CartItemDTO에서 builder로 쇼핑카트 엔티티를 CartItemDTO 로 변환해서
        이 CartItem클래스의 getTotalPrice메서드의 계산 값을 CartItemDTO객체의 totalPrice에 담음
         */

        //quantity가 변동돼서
    }

}



