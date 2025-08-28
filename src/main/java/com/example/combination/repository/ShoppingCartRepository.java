package com.example.combination.repository;


import com.example.combination.domain.order.CartItem;
import com.example.combination.domain.order.ShoppingCart;
import com.example.combination.dto.OrderItemDTO;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ShoppingCartRepository {

    private final EntityManager em;

    public void save(ShoppingCart shoppingCart, CartItem cartItem) {
        em.persist(shoppingCart);
    }

    public void delete(ShoppingCart shoppingCart, CartItem cartItem) {
        em.remove(shoppingCart);
    }

    //상품 선택 정보

//
//    ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
//
//    CartItem cartItem = CartItem.fromDTO(dto, cart);
//cart.addItem(cartItem);          // Cart에 추가
//cartRepository.save(cart);       // DB 저장

}
