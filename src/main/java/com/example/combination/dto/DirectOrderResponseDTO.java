package com.example.combination.dto;

import com.example.combination.domain.order.OrderStatus;
import com.example.combination.domain.payment.PaymentStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectOrderResponseDTO {
    
    private Long orderId;
    private Long paymentId;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private int totalAmount;
    private int finalPrice;
    private String message;
    
    // PG사 결제 정보
    private String pgTransactionId;
    private String redirectUrl; // PG사 결제 페이지 URL
    
    // 결제 완료 정보
    private String paymentMethod;
    private String serviceType;
    private boolean success;
    private String errorMessage;
}
