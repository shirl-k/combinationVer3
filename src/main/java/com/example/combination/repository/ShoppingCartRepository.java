package com.example.combination.repository;


import com.example.combination.domain.order.ShoppingCart;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ShoppingCartRepository {

    private final EntityManager em;

    public void save(ShoppingCart shoppingCart) {
        em.persist(shoppingCart);
    }


    //PK (cartId) 조회
    public Optional<ShoppingCart> findById(Long cartId) {
        return Optional.ofNullable(em.find(ShoppingCart.class, cartId));
    }





    //상품 선택 정보

//dd
//    ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
//
//    CartItem cartItem = CartItem.fromDTO(dto, cart);
//cart.addItem(cartItem);          // Cart에 추가
//cartRepository.save(cart);       // DB 저장

}
