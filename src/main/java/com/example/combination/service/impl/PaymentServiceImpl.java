package com.example.combination.service.impl;

import com.example.combination.domain.order.Order;
import com.example.combination.domain.order.OrderStatus;
import com.example.combination.domain.payment.Payment;
import com.example.combination.domain.payment.PaymentStatus;
import com.example.combination.dto.*;
import com.example.combination.exception.OrderNotFoundException;
import com.example.combination.exception.PaymentException;
import com.example.combination.exception.PaymentNotFoundException;
import com.example.combination.repository.OrderRepository;
import com.example.combination.repository.PaymentRepository;
import com.example.combination.service.OrderService;
import com.example.combination.service.PaymentService;
import com.example.combination.service.PgApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final PgApiService pgApiService;
    
    @Override
    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequest) {
        try {
            log.info("결제 처리 시작: orderId={}, paymentMethod={}", 
                    paymentRequest.getOrderId(), paymentRequest.getPaymentMethod());
            
            // 주문 조회
            Order order = orderRepository.findByOrderId(paymentRequest.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException(paymentRequest.getOrderId()));
            
            // 결제 금액 계산
            int paymentAmount = order.calculateLineTotalPrice();
            if (paymentRequest.isUsePoints()) {
                paymentAmount = order.getFinalPrice();
            }
            
            // 결제 엔티티 생성
            Payment payment = Payment.createPayment(order, paymentRequest.getPaymentMethod(), paymentAmount);
            payment = paymentRepository.save(payment);
            
            // PG사별 결제 처리
            PaymentResponseDTO response = processPaymentByMethod(payment, paymentRequest);
            
            // 결제 상태 업데이트
            if (response.getPaymentStatus() == PaymentStatus.PAID) {
                payment.updateStatus(PaymentStatus.PAID);
                payment.setPgResponse(response.getPgTransactionId(), response.getPgApprovalNumber(), response.getMessage());
                
                // 주문 상태 업데이트
                orderService.confirmOrder(order.getOrderId());
            } else if (response.getPaymentStatus() == PaymentStatus.FAILED) {
                payment.updateStatus(PaymentStatus.FAILED);
                payment.setFailureReason(response.getFailureReason());
            }
            
            paymentRepository.save(payment);
            
            return PaymentResponseDTO.builder()
                    .paymentId(payment.getId())
                    .orderId(payment.getOrder().getOrderId())
                    .paymentStatus(payment.getPaymentStatus())
                    .amount(payment.getAmount())
                    .pgTransactionId(payment.getPgTransactionId())
                    .pgApprovalNumber(payment.getPgApprovalNumber())
                    .message(response.getMessage())
                    .redirectUrl(response.getRedirectUrl())
                    .failureReason(response.getFailureReason())
                    .build();
                    
        } catch (Exception e) {
            log.error("결제 처리 실패: orderId={}", paymentRequest.getOrderId(), e);
            throw new PaymentException("결제 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    private PaymentResponseDTO processPaymentByMethod(Payment payment, PaymentRequestDTO paymentRequest) {
        switch (paymentRequest.getPaymentMethod()) {
            case CARD:
                return processCardPayment(payment, paymentRequest);
            case BANK_TRANSFER:
                return processBankTransferPayment(payment, paymentRequest);
            case POINT:
                return processPointPayment(payment, paymentRequest);
            default:
                throw new PaymentException("지원하지 않는 결제 방식입니다: " + paymentRequest.getPaymentMethod());
        }
    }
    
    private PaymentResponseDTO processCardPayment(Payment payment, PaymentRequestDTO paymentRequest) {
        try {
            // 카드 결제는 PG사 API 호출
            PgPaymentRequestDTO pgRequest = PgPaymentRequestDTO.builder()
                    .orderId(payment.getOrder().getOrderId())
                    .amount(payment.getAmount())
                    .paymentMethod(paymentRequest.getPaymentMethod())
                    .customerName(payment.getOrder().getMember().getUserInfo().getNickname())
                    .customerEmail(payment.getOrder().getMember().getUserInfo().getEmail())
                    .build();
            
            PgPaymentResponseDTO pgResponse = pgApiService.requestPayment(pgRequest);
            
            if (pgResponse.isSuccess()) {
                return PaymentResponseDTO.builder()
                        .paymentStatus(PaymentStatus.PENDING)
                        .pgTransactionId(pgResponse.getTransactionId())
                        .redirectUrl(pgResponse.getRedirectUrl())
                        .message("카드 결제 요청이 성공했습니다.")
                        .build();
            } else {
                return PaymentResponseDTO.builder()
                        .paymentStatus(PaymentStatus.FAILED)
                        .failureReason(pgResponse.getErrorMessage())
                        .message("카드 결제 요청이 실패했습니다.")
                        .build();
            }
            
        } catch (Exception e) {
            log.error("카드 결제 처리 실패", e);
            return PaymentResponseDTO.builder()
                    .paymentStatus(PaymentStatus.FAILED)
                    .failureReason(e.getMessage())
                    .message("카드 결제 처리 중 오류가 발생했습니다.")
                    .build();
        }
    }
    
    private PaymentResponseDTO processBankTransferPayment(Payment payment, PaymentRequestDTO paymentRequest) {
        try {
            // 무통장입금은 즉시 완료 처리
            return PaymentResponseDTO.builder()
                    .paymentStatus(PaymentStatus.PAID)
                    .pgTransactionId("BANK_" + System.currentTimeMillis())
                    .message("무통장입금이 완료되었습니다.")
                    .build();
                    
        } catch (Exception e) {
            log.error("무통장입금 처리 실패", e);
            return PaymentResponseDTO.builder()
                    .paymentStatus(PaymentStatus.FAILED)
                    .failureReason(e.getMessage())
                    .message("무통장입금 처리 중 오류가 발생했습니다.")
                    .build();
        }
    }
    
    private PaymentResponseDTO processPointPayment(Payment payment, PaymentRequestDTO paymentRequest) {
        try {
            // 포인트 결제는 즉시 완료 처리
            return PaymentResponseDTO.builder()
                    .paymentStatus(PaymentStatus.PAID)
                    .pgTransactionId("POINT_" + System.currentTimeMillis())
                    .message("포인트 결제가 완료되었습니다.")
                    .build();
                    
        } catch (Exception e) {
            log.error("포인트 결제 처리 실패", e);
            return PaymentResponseDTO.builder()
                    .paymentStatus(PaymentStatus.FAILED)
                    .failureReason(e.getMessage())
                    .message("포인트 결제 처리 중 오류가 발생했습니다.")
                    .build();
        }
    }
    
    @Override
    public PaymentResponseDTO getPaymentStatus(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        
        return PaymentResponseDTO.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrder().getOrderId())
                .paymentStatus(payment.getPaymentStatus())
                .amount(payment.getAmount())
                .pgTransactionId(payment.getPgTransactionId())
                .pgApprovalNumber(payment.getPgApprovalNumber())
                .message("결제 정보 조회 성공")
                .build();
    }
    
    @Override
    public boolean cancelPayment(Long paymentId, String reason) {
        try {
            log.info("결제 취소 시작: paymentId={}, reason={}", paymentId, reason);
            
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new PaymentNotFoundException(paymentId));
            
            if (payment.getPaymentStatus() != PaymentStatus.PAID) {
                throw new PaymentException("결제 완료된 건만 취소할 수 있습니다.");
            }
            
            // PG사 취소 요청
            PgPaymentResponseDTO pgResponse = pgApiService.cancelPayment(
                    payment.getPgTransactionId(), reason);
            
            if (pgResponse.isSuccess()) {
                payment.updateStatus(PaymentStatus.CANCELLED);
                paymentRepository.save(payment);
                
                // 주문 취소 - null 대신 주문의 첫 번째 아이템 사용
                Order order = payment.getOrder();
                if (order != null && !order.getOrderItems().isEmpty()) {
                    orderService.removeOrder(order.getOrderId(), order.getOrderItems().get(0));
                } else {
                    // 주문 아이템이 없는 경우 주문만 취소
                    orderService.cancelOrder(order.getOrderId());
                }
                
                log.info("결제 취소 완료: paymentId={}", paymentId);
                return true;
            } else {
                log.error("PG사 결제 취소 실패: {}", pgResponse.getErrorMessage());
                return false;
            }
            
        } catch (Exception e) {
            log.error("결제 취소 실패: paymentId={}", paymentId, e);
            return false;
        }
    }
    
    @Override
    public boolean refundPayment(Long paymentId, int amount, String reason) {
        try {
            log.info("결제 환불 시작: paymentId={}, amount={}, reason={}", paymentId, amount, reason);
            
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new PaymentNotFoundException(paymentId));
            
            if (payment.getPaymentStatus() != PaymentStatus.PAID) {
                throw new PaymentException("결제 완료된 건만 환불할 수 있습니다.");
            }
            
            // PG사 환불 요청
            PgPaymentResponseDTO pgResponse = pgApiService.refundPayment(
                    payment.getPgTransactionId(), amount, reason);
            
            if (pgResponse.isSuccess()) {
                payment.updateStatus(PaymentStatus.REFUNDED);
                payment.setRefundTransactionId(pgResponse.getTransactionId());
                paymentRepository.save(payment);
                
                log.info("결제 환불 완료: paymentId={}", paymentId);
                return true;
            } else {
                log.error("PG사 결제 환불 실패: {}", pgResponse.getErrorMessage());
                return false;
            }
            
        } catch (Exception e) {
            log.error("결제 환불 실패: paymentId={}", paymentId, e);
            return false;
        }
    }
    
    @Override
    public boolean handlePaymentCallback(PaymentCallbackDTO callback) {
        try {
            log.info("결제 콜백 처리: transactionId={}, status={}", 
                    callback.getTransactionId(), callback.getStatus());
            
            Payment payment = paymentRepository.findByPgTransactionId(callback.getTransactionId())
                    .orElseThrow(() -> new PaymentNotFoundException("거래 ID: " + callback.getTransactionId()));
            
            // 결제 상태 업데이트
            if ("SUCCESS".equals(callback.getStatus())) {
                payment.updateStatus(PaymentStatus.PAID);
                payment.setPgResponse(callback.getTransactionId(), callback.getApprovalNumber(), callback.getRawData());
                
                // 주문 상태 업데이트
                orderService.confirmOrder(payment.getOrder().getOrderId());
                
                log.info("결제 콜백 처리 완료: paymentId={}", payment.getId());
            } else {
                payment.updateStatus(PaymentStatus.FAILED);
                payment.setFailureReason("결제 실패: " + callback.getStatus());
                
                log.warn("결제 콜백 실패: paymentId={}, status={}", payment.getId(), callback.getStatus());
            }
            
            paymentRepository.save(payment);
            return true;
                    
        } catch (Exception e) {
            log.error("결제 콜백 처리 실패: transactionId={}", callback.getTransactionId(), e);
            return false;
        }
    }
}