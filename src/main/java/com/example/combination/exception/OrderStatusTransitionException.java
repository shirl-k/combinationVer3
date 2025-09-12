package com.example.combination.exception;

import com.example.combination.domain.order.OrderStatus;

/**
 * 주문 상태 전환 예외
 * 허용되지 않는 주문 상태 전환을 시도할 때 발생
 */
public class OrderStatusTransitionException extends BusinessException {
    
    private final OrderStatus fromStatus;
    private final OrderStatus toStatus;
    
    public OrderStatusTransitionException(OrderStatus fromStatus, OrderStatus toStatus) {
        super(String.format("주문 상태 전환 불가: %s -> %s", fromStatus, toStatus));
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
    }
    
    public OrderStatus getFromStatus() {
        return fromStatus;
    }
    
    public OrderStatus getToStatus() {
        return toStatus;
    }
}
