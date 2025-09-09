package com.example.combination.service;

import com.example.combination.domain.delivery.JustDelivery;
import com.example.combination.domain.delivery.ServiceType;
import com.example.combination.domain.item.SKU;
import com.example.combination.domain.member.Member;
import com.example.combination.domain.movingService.MovingService;
import com.example.combination.domain.order.Order;
import com.example.combination.domain.order.OrderItem;
import com.example.combination.domain.order.OrderStatus;
import com.example.combination.domain.payment.PaymentStatus;
import com.example.combination.domain.valuetype.DeliveryAddress;
import com.example.combination.domain.valuetype.HomeAddress;
import com.example.combination.domain.valuetype.MovingServiceAddress;
import com.example.combination.dto.*;
import com.example.combination.exception.SKUNotFoundException;
import com.example.combination.repository.MemberRepository;
import com.example.combination.repository.OrderRepository;
import com.example.combination.repository.SKURepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DirectOrderService {
    
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final SKURepository skuRepository;
    private final PaymentService paymentService;
    
    /**
     * 바로 결제 처리
     */
    public DirectOrderResponseDTO processDirectOrder(DirectOrderRequestDTO request) {
        try {
            log.info("바로 결제 처리 시작: skuId={}, quantity={}, serviceType={}, paymentMethod={}", 
                    request.getSkuId(), request.getQuantity(), request.getServiceType(), request.getPaymentMethod());
            
            // 1. 상품 정보 조회
            SKU sku = skuRepository.findBySkuId(request.getSkuId())
                    .orElseThrow(() -> new SKUNotFoundException(request.getSkuId()));
            
            // 2. 회원 정보 조회 (임시로 기본 회원 사용)
            Member member = getDefaultMember();
            
            // 3. 주문 아이템 생성
            OrderItem orderItem = createOrderItem(sku, request);
            
            // 4. 주문 생성
            Order order = createOrder(member, orderItem, request);
            
            // 5. 서비스 정보 설정
            setServiceInfo(order, request);
            
            // 6. 주문을 영속성 컨텍스트에 추가 (변경감지용)
            orderRepository.persistOrder(order);
            
            // 7. 결제 처리
            PaymentRequestDTO paymentRequest = createPaymentRequest(order, request);
            PaymentResponseDTO paymentResponse = paymentService.processPayment(paymentRequest);
            
            // 8. 결제 결과에 따른 주문 상태 업데이트 (변경감지 활용)
            updateDirectOrderStatus(order, paymentResponse);
            
            // 9. 응답 생성
            return createDirectOrderResponse(order, paymentResponse);
            
        } catch (Exception e) {
            log.error("바로 결제 처리 실패", e);
            return DirectOrderResponseDTO.builder()
                    .success(false)
                    .errorMessage(e.getMessage())
                    .message("바로 결제 처리 중 오류가 발생했습니다.")
                    .build();
        }
    }
    
    /**
     * 주문 아이템 생성
     */
    private OrderItem createOrderItem(SKU sku, DirectOrderRequestDTO request) {
        return OrderItem.builder()
                .sku(sku)
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .selected(true)
                .build();
    }
    
    /**
     * 주문 생성
     */
    private Order createOrder(Member member, OrderItem orderItem, DirectOrderRequestDTO request) {
        Order order = Order.builder()
                .member(member)
                .serviceType(request.getServiceType())
                .paymentMethod(request.getPaymentMethod())
                .orderStatus(OrderStatus.CREATED)
                .usePoints(request.isUsePoints())
                .createOrderDate(LocalDate.now())
                .build();
        
        order.addOrderItem(orderItem);
        return order;
    }
    
    /**
     * 서비스 정보 설정
     */
    private void setServiceInfo(Order order, DirectOrderRequestDTO request) {
        if (request.getServiceType() == ServiceType.JUST_DELIVERY) {
            setJustDeliveryInfo(order, request);
        } else if (request.getServiceType() == ServiceType.MOVING_SERVICE) {
            setMovingServiceInfo(order, request);
        }
    }
    
    /**
     * 일반 배송 정보 설정
     */
    private void setJustDeliveryInfo(Order order, DirectOrderRequestDTO request) {
        JustDelivery justDelivery = JustDelivery.builder()
                .deliveryAddress(DeliveryAddress.builder()
                        .roadNameAddress(request.getDeliveryAddress())
                        .build())
                .build();
        
        order.setJustDelivery(justDelivery);
    }
    
    /**
     * 이사 서비스 정보 설정
     */
    private void setMovingServiceInfo(Order order, DirectOrderRequestDTO request) {
        MovingService movingService = MovingService.builder()
                .name(request.getCustomerName())
                .phoneNum(request.getCustomerPhone())
                .movingServiceDescription(request.getMovingDescription())
                .homeAddress(HomeAddress.builder()
                        .roadNameAddress(request.getHomeAddress())
                        .build())
                .movingServiceAddress(MovingServiceAddress.builder()
                        .roadNameAddress(request.getDeliveryAddress())
                        .build())
                .build();
        
        order.setMovingService(movingService);
    }
    
    /**
     * 결제 요청 생성
     */
    private PaymentRequestDTO createPaymentRequest(Order order, DirectOrderRequestDTO request) {
        return PaymentRequestDTO.builder()
                .orderId(order.getOrderId())
                .paymentMethod(request.getPaymentMethod())
                .usePoints(request.isUsePoints())
                .build();
    }
    
    /**
     * 바로 결제 주문 상태 업데이트 (변경감지 활용)
     * @Transactional에 의해 트랜잭션 커밋 시 자동으로 변경감지됨
     */
    private void updateDirectOrderStatus(Order order, PaymentResponseDTO paymentResponse) {
        if (paymentResponse.getPaymentStatus() == PaymentStatus.PAID) {
            order.changeOrderStatus(OrderStatus.CONFIRMED);
            log.info("주문 상태 업데이트: {} -> CONFIRMED (변경감지)", order.getOrderId());
        } else if (paymentResponse.getPaymentStatus() == PaymentStatus.FAILED) {
            order.changeOrderStatus(OrderStatus.CANCELLED);
            log.info("주문 상태 업데이트: {} -> CANCELLED (변경감지)", order.getOrderId());
        } else if (paymentResponse.getPaymentStatus() == PaymentStatus.PENDING) {
            // PENDING 상태는 주문 상태를 그대로 유지
            log.info("주문 상태 유지: {} -> {} (결제 대기 중)", order.getOrderId(), order.getOrderStatus());
        }
    }
    
    /**
     * 바로 결제 응답 생성
     */
    private DirectOrderResponseDTO createDirectOrderResponse(Order order, PaymentResponseDTO paymentResponse) {
        return DirectOrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(paymentResponse.getPaymentStatus())
                .totalAmount(order.calculateLineTotalPrice())
                .finalPrice(order.getFinalPrice())
                .message(paymentResponse.getMessage())
                .pgTransactionId(paymentResponse.getPgTransactionId())
                .redirectUrl(paymentResponse.getRedirectUrl())
                .paymentMethod(order.getPaymentMethod() != null ? order.getPaymentMethod().toString() : null)
                .serviceType(order.getServiceType() != null ? order.getServiceType().toString() : null)
                .success(paymentResponse.getPaymentStatus() == PaymentStatus.PAID || 
                        paymentResponse.getPaymentStatus() == PaymentStatus.PENDING)
                .errorMessage(paymentResponse.getFailureReason())
                .build();
    }
    
    /**
     * 기본 회원 조회 (임시 구현)
     */
    private Member getDefaultMember() {
        // 실제 구현에서는 현재 로그인한 사용자를 가져와야 함
        return memberRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("기본 회원을 찾을 수 없습니다."));
    }
}