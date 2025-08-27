package com.example.combination.repository;


import com.example.combination.dto.OrderItemDTO;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShoppingCartRepository {

    private final EntityManager em;

    private OrderItemDTO orderItemsDTO; //상품 선택 정보

//
//    ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
//
//    CartItem cartItem = CartItem.fromDTO(dto, cart);
//cart.addItem(cartItem);          // Cart에 추가
//cartRepository.save(cart);       // DB 저장

}
