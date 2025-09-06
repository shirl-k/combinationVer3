package com.example.combination.service;

import com.example.combination.domain.delivery.DeliveryForm;
import com.example.combination.domain.delivery.DeliveryStatus;

import com.example.combination.exception.DeliveryNotFoundException;
import com.example.combination.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    //변경 감지
    @Transactional
    public void updateDeliveryStatus(Long deliveryId, DeliveryStatus newStatus) {
        DeliveryForm deliveryForm = deliveryRepository.findByDeliveryId(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
        deliveryForm.changeDeliveryStatus(newStatus);
    }
}

    //상태 변경 READY(배송 준비 중: 상품 패킹),SHIPPED(출고됨(운송장 번호 생성)),IN_TRANSIT(배송 중),DELIVERED(배송 완료),RETURNED(반송됨)

    //SHIPPED 출고됨



//    public Order createOrder(Member member, List<OrderItem> orderItems, PaymentMethod paymentMethod
//            , DeliveryAddressForm deliveryAddressForm, OrderStatus orderStatus, boolean usePoints, LocalDateTime createOrderDate, int usedPoints) {
//        Order order = Order.createFinalOrder(member, orderItems,paymentMethod,deliveryAddressForm,orderStatus,usePoints,usedPoints);
//
//        order.calculateFinalPrice(member); //최종 금액 계산(포인트 반영)
//        orderRepository.save(order);
//        return order;
//    }

