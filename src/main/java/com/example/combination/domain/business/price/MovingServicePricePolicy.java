package com.example.combination.domain.business.price;

import com.example.combination.domain.order.Order;
import org.springframework.stereotype.Component;

@Component
public class MovingServicePricePolicy implements ServicePricePolicy {
    @Override
    public int calculatePrice(Order order) {
        return order.getBasePrice() + order.getMovingService().calculateMovingServicePrice();
    }
}
