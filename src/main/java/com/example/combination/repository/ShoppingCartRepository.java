package com.example.combination.repository;


import com.example.combination.domain.order.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    List<ShoppingCart> findByMemberIdAndCartId(Long userId, Long cartId);

}
