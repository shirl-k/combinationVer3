package com.example.combination.domain.order;

import com.example.combination.domain.item.Item;
import com.example.combination.dto.CartItemDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orderItem")
public class OrderItem { //결제 시 스냅샷(결제 당시 금액 영수증처럼 그대로 찍어내기. 가격 변동 고려)

    @Id
    @GeneratedValue
    private Long id;

    private Long skuId;  //재고 관리 단위

    private String name;

    private int unitPrice; //단가.  //주문 당시 가격 (상품 가격 변동 고려)

    private int quantity; //주문 수량

    private int totalPrice;

    private int discountPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    //============핵심 비즈니스 로직 ==============//
    
    //CartDTO를 OrderItem엔티티로 변환 (영속 데이터-> OrderItemRepository에서 DB 저장 로직)
    public static OrderItem fromDTO(CartItemDTO cartItemDTO) {
        return OrderItem.builder()
                .skuId(cartItemDTO.getId())
                .name(cartItemDTO.getItemName())
                .unitPrice(cartItemDTO.getUnitPrice())
                .quantity(cartItemDTO.getQuantity())
                .totalPrice(cartItemDTO.getTotalPrice())
                .build();

    }

    //


//    public Order getOrder(Item item, int orderQuantity, int unitPrice) {
//        return Order.builder()
//                .item(item)
//                .orderQuantity(orderQuantity)
//                .unitPrice(unitPrice)
//                .build();
//
//    }
    //단건 결제

}

//
//    public OrderItem createOrderItem(
//            Item item,
//            int unitPrice,
//            int orderQuantity
//            ) {
//        return OrderItem.builder()
//                .item(item)
//                .unitPrice(unitPrice)
//                .orderQuantity(orderQuantity)
//                .build();
    //OrderItem 주문항목 객체 만듦





