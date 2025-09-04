package com.example.combination.restcontroller;

import com.example.combination.domain.order.Order;
import com.example.combination.dto.DeliveryAddressFormDTO;
import com.example.combination.service.OrderService;
import com.example.combination.web.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController("order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/cart/{cartId}/order/{orderId}/address")
    public ResponseEntity<ApiResponse<Void>> setDelivery(@PathVariable Long orderId,
                                                   @RequestBody DeliveryAddressFormDTO deliveryAddressFormDTO) {

    }


}
