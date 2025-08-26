package com.example.combination.repository;

import com.example.combination.domain.order.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByUserIdAndCartId(Long userId, Long cartId);

}
