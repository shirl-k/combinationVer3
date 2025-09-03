package com.example.combination.service;

import com.example.combination.domain.delivery.Delivery;
import com.example.combination.domain.member.Member;
import com.example.combination.domain.order.Order;
import com.example.combination.domain.order.OrderItem;
import com.example.combination.domain.order.OrderStatus;
import com.example.combination.exception.OrderNotFoundException;
import com.example.combination.repository.MemberRepository;
import com.example.combination.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

    @Transactional
    @Service
    @RequiredArgsConstructor
    public class OrderService {

        private final OrderRepository orderRepository;
        private final MemberRepository memberRepository;

            //주문 수정 : 변경감지
            public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
            Order order = orderRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new OrderNotFoundException(orderId));

            order.changeOrderStatus(newStatus);  //변경 감지 (JPA가 영속 상태 엔티티 조회로 감시해서 커밋시점에 변경된 데이터 업데이트)
            }

             //주문 승인
            public void confirmOrder(Long orderId) {
                Order order = orderRepository.findByOrderId(orderId)
                        .orElseThrow(() -> new OrderNotFoundException(orderId));

                order.changeOrderStatus(OrderStatus.CONFIRMED);

                //주문 승인 시 주문한 회원 조회해서 결제 금액 누적 합산, 등급 관리
                Member member = order.getMember();

                order.getUsedPoints()
            }
//            int amount = order.calculateLineTotalPrice();

//
//            member.addTotalSpent(amount); //누적 금액 합산 + 자동 등급 재계산
//
//                int newPoints = amount * 5/100;
//                member.addMemberPoints(newPoints); //포인트 적립


        //주문 취소
        public void removeOrder(Long orderId, OrderItem orderItem) {
            Order order = orderRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new OrderNotFoundException(orderId));

            order.removeOrderItem(orderItem);
            order.changeOrderStatus(OrderStatus.CANCELLED);

        }

        //주문 완료
        public void completeOrder(Long orderId) {
            Order order = orderRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new OrderNotFoundException(orderId));

            order.changeOrderStatus(OrderStatus.COMPLETED);

            //Delivery

        }
        }

