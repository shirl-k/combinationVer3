package com.example.combination.restcontroller;

import com.example.combination.domain.order.ShoppingCart;
import com.example.combination.dto.OrderItemDTO;
import com.example.combination.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cart")
@RequiredArgsConstructor

public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping("/items")
    public ResponseEntity<Void> addItemToCart(@RequestBody OrderItemDTO orderItemDTO) {
        shoppingCartService.addItem(orderItemDTO);
        return ResponseEntity.ok().build();
    }
}
