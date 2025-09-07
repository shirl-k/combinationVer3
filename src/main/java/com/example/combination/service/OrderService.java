package com.example.combination.service;

import com.example.combination.domain.business.price.JustDeliveryPricePolicy;
import com.example.combination.domain.business.price.MovingServicePricePolicy;
import com.example.combination.domain.delivery.ServiceType;
import com.example.combination.domain.member.Member;
import com.example.combination.domain.order.Order;
import com.example.combination.domain.order.OrderItem;
import com.example.combination.domain.order.OrderStatus;
import com.example.combination.domain.payment.PaymentMethod;
import com.example.combination.exception.OrderNotFoundException;
import com.example.combination.repository.MemberRepository;
import com.example.combination.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

    @Transactional
    @Service
    @RequiredArgsConstructor
    public class OrderService {

        private final OrderRepository orderRepository;
        private final MemberRepository memberRepository;
        private final JustDeliveryPricePolicy justDeliveryPricePolicy;
        private final MovingServicePricePolicy movingServicePricePolicy;

            //주문 수정 : 변경감지
            public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
            Order order = orderRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new OrderNotFoundException(orderId));

            order.changeOrderStatus(newStatus);  //변경 감지 (JPA가 영속 상태 엔티티 조회로 감시해서 커밋시점에 변경된 데이터 업데이트)
            }

            public int calculateFinalPrice(Order order) {
                if(order.getServiceType() == ServiceType.JUST_DELIVERY) {
                    return justDeliveryPricePolicy.calculatePrice(order);
                }else if(order.getServiceType() == ServiceType.MOVING_SERVICE) {
                    return movingServicePricePolicy.calculatePrice(order);
                }else throw new IllegalStateException("배송 서비스 정보를 찾을 수 없습니다.");
            }

            //주문 생성 : CREATED OrderService: (흐름/연동)

//            public Order createOrder(Member member, List<OrderItem> orderItems, PaymentMethod paymentMethod
//                    , //JustDelivery justDelivery, MovingService movingService, OrderStatus orderStatus, boolean usePoints, LocalDateTime createOrderDate, int usedPoints) {
//                Order order = Order.createFinalOrder(member, orderItems,paymentMethod, deliveryForm,orderStatus,usePoints,usedPoints);
//
//                order.calculateFinalPrice(member); //최종 금액 계산(포인트 반영)
//                orderRepository.save(order);
//                return order;
//            }
//

             //주문 승인 : CONFIRMED
            public void confirmOrder(Long orderId) {
                Order order = orderRepository.findByOrderId(orderId)
                        .orElseThrow(() -> new OrderNotFoundException(orderId));


                //주문 승인 시 주문한 회원 조회해서 결제 금액 누적 합산, 등급 관리
                Member member = order.getMember();

                //포인트 적용 최종 가격
                order.calculateFinalPrice(member);
                //포인트 사용 시
                    if(order.isUsePoints()) {
                        int pointsToUse = order.getUsedPoints();
                        //누적 포인트에서 차감
                        member.usePoints(pointsToUse);
                    }
                    //누적 결제금액 합계: 누적 금액 + 결제 금액 , 등급 계산
                    int amount = order.getFinalPrice();
                    member.addTotalSpent(amount);
                    
                    int newPoints = amount * 5/100; //새 포인트 적립
                    member.addMemberPoints(newPoints);

                //주문 상태 변경
                order.changeOrderStatus(OrderStatus.CONFIRMED);
                orderRepository.save(order);
            }

        //주문 취소 CANCELLED
        public void removeOrder(Long orderId, OrderItem orderItem) {
            Order order = orderRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new OrderNotFoundException(orderId));
            order.removeOrderItem(orderItem);

            //주문 취소 시 포인트 롤백
            if(order.isUsePoints() && order.getUsedPoints() > 0) {
                order.getMember().addMemberPoints(order.getUsedPoints());
            }

            order.changeOrderStatus(OrderStatus.CANCELLED);

        }

        //주문 완료 COMPLETED 
        public void completeOrder(Long orderId) {
            Order order = orderRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new OrderNotFoundException(orderId));

            order.changeOrderStatus(OrderStatus.COMPLETED);

        }
        }

