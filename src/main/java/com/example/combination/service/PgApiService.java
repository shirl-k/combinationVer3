package com.example.combination.service;

import com.example.combination.dto.PgPaymentRequestDTO;
import com.example.combination.dto.PgPaymentResponseDTO;
import com.example.combination.dto.PaymentCallbackDTO;

public interface PgApiService {
    
    /**
     * PG사에 결제 요청
     * @param request 결제 요청 정보
     * @return PG사 응답
     */
    PgPaymentResponseDTO requestPayment(PgPaymentRequestDTO request);
    
    /**
     * PG사 결제 승인
     * @param transactionId 거래 ID
     * @param approvalNumber 승인번호
     * @return 승인 결과
     */
    PgPaymentResponseDTO approvePayment(String transactionId, String approvalNumber);
    
    /**
     * PG사 결제 취소
     * @param transactionId 거래 ID
     * @param reason 취소 사유
     * @return 취소 결과
     */
    PgPaymentResponseDTO cancelPayment(String transactionId, String reason);
    
    /**
     * PG사 환불
     * @param transactionId 거래 ID
     * @param amount 환불 금액
     * @param reason 환불 사유
     * @return 환불 결과
     */
    PgPaymentResponseDTO refundPayment(String transactionId, int amount, String reason);
    
    /**
     * PG사 서명 검증
     * @param callback 콜백 데이터
     * @return 검증 결과
     */
    boolean verifySignature(PaymentCallbackDTO callback);
}
