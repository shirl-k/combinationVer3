package com.example.combination.service;

import com.example.combination.domain.order.Order;
import com.example.combination.domain.order.OrderStatus;
import com.example.combination.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;


    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("주문 없음"));
        
        order.setOrderStatus(newStatus);  //변경 감지 (JPA가 영속 상태 엔티티 조회로 감시해서 커밋시점에 변경된 데이터 업데이트)
    }


}
