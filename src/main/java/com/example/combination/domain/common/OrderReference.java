package com.example.combination.domain.common;

import com.example.combination.domain.order.OrderStatus;

/**
 * 주문 참조를 위한 인터페이스
 * 순환참조를 방지하기 위해 Order 엔티티 대신 인터페이스 사용
 */
public interface OrderReference {
    
    /**
     * 주문 ID 반환
     */
    Long getOrderId();
    
    /**
     * 주문 상태 반환
     */
    OrderStatus getOrderStatus();
    
    /**
     * 주문이 존재하는지 확인
     */
    default boolean hasOrder() {
        return getOrderId() != null;
    }
    
    /**
     * 주문이 특정 상태인지 확인
     */
    default boolean isOrderStatus(OrderStatus status) {
        return hasOrder() && getOrderStatus() == status;
    }
}
