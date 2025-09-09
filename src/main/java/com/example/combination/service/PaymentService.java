package com.example.combination.service;

import com.example.combination.dto.PaymentRequestDTO;
import com.example.combination.dto.PaymentResponseDTO;
import com.example.combination.dto.PaymentCallbackDTO;

public interface PaymentService {
    
    /**
     * 결제 요청 처리
     * @param paymentRequest 결제 요청 정보
     * @return 결제 응답 정보
     */
    PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequest);
    
    /**
     * PG사 결제 콜백 처리
     * @param callback PG사 콜백 데이터
     * @return 처리 결과
     */
    boolean handlePaymentCallback(PaymentCallbackDTO callback);
    
    /**
     * 결제 취소
     * @param paymentId 결제 ID
     * @param reason 취소 사유
     * @return 취소 결과
     */
    boolean cancelPayment(Long paymentId, String reason);
    
    /**
     * 결제 환불
     * @param paymentId 결제 ID
     * @param amount 환불 금액
     * @param reason 환불 사유
     * @return 환불 결과
     */
    boolean refundPayment(Long paymentId, int amount, String reason);
    
    /**
     * 결제 상태 조회
     * @param paymentId 결제 ID
     * @return 결제 상태
     */
    PaymentResponseDTO getPaymentStatus(Long paymentId);
}