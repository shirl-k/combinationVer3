package com.example.combination.service;

import com.example.combination.repository.CartItemRepository;
import com.example.combination.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartService {



    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;




}
