package com.example.combination.domain.business.price;

import com.example.combination.domain.order.Order;

public interface ServicePricePolicy {
    int calculatePrice(Order order);
}
