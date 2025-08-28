package com.example.combination.service;

import com.example.combination.domain.order.CartItem;
import com.example.combination.domain.order.ShoppingCart;
import com.example.combination.repository.CartItemRepository;
import com.example.combination.repository.ItemRepository;
import com.example.combination.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartService {


    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    private void updateShoppingCart(CartItem cartItem) {
        ShoppingCart shoppingCart

}
