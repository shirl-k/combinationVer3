package com.example.combination.service.impl;

import com.example.combination.domain.order.Order;
import com.example.combination.domain.payment.Payment;
import com.example.combination.domain.payment.PaymentMethod;
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

@Service
@RequiredArgsConstructor
@Slf4j
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
                    .orderId(order.getOrderId())
                    .paymentStatus(payment.getPaymentStatus())
                    .pgTransactionId(response.getPgTransactionId())
                    .pgApprovalNumber(response.getPgApprovalNumber())
                    .amount(paymentAmount)
                    .message(response.getMessage())
                    .redirectUrl(response.getRedirectUrl())
                    .failureReason(response.getFailureReason())
                    .build();
                    
        } catch (Exception e) {
            log.error("결제 처리 실패: orderId={}", paymentRequest.getOrderId(), e);
            throw new PaymentException("결제 처리 중 오류가 발생했습니다.", e);
        }
    }
    
    private PaymentResponseDTO processPaymentByMethod(Payment payment, PaymentRequestDTO request) {
        switch (request.getPaymentMethod()) {
            case CARD:
                return processCardPayment(payment, request);
            case BANK_TRANSFER:
                return processBankTransferPayment(payment, request);
            case POINT:
                return processPointPayment(payment, request);
            default:
                throw new PaymentException("지원하지 않는 결제 방식입니다: " + request.getPaymentMethod());
        }
    }
    
    private PaymentResponseDTO processCardPayment(Payment payment, PaymentRequestDTO request) {
        try {
            // PG사 결제 요청 데이터 구성
            PgPaymentRequestDTO pgRequest = PgPaymentRequestDTO.builder()
                    .orderId(payment.getOrder().getOrderId())
                    .orderName("인테리어 쇼핑몰 주문")
                    .amount(payment.getAmount())
                    .paymentMethod(PaymentMethod.CARD)
                    .customerName(payment.getOrder().getMember().getName())
                    .customerEmail(payment.getOrder().getMember().getUserInfo().getEmail())
                    .successUrl("http://localhost:8080/payment/success")
                    .failUrl("http://localhost:8080/payment/fail")
                    .cancelUrl("http://localhost:8080/payment/cancel")
                    .build();
            
            // PG사 API 호출
            PgPaymentResponseDTO pgResponse = pgApiService.requestPayment(pgRequest);
            
            if (pgResponse.isSuccess()) {
                return PaymentResponseDTO.builder()
                        .paymentStatus(PaymentStatus.PENDING)
                        .pgTransactionId(pgResponse.getTransactionId())
                        .redirectUrl(pgResponse.getRedirectUrl())
                        .message("카드 결제 페이지로 이동하세요.")
                        .build();
            } else {
                return PaymentResponseDTO.builder()
                        .paymentStatus(PaymentStatus.FAILED)
                        .failureReason(pgResponse.getErrorMessage())
                        .message("카드 결제 요청 실패")
                        .build();
            }
        } catch (Exception e) {
            log.error("카드 결제 처리 실패", e);
            return PaymentResponseDTO.builder()
                    .paymentStatus(PaymentStatus.FAILED)
                    .failureReason(e.getMessage())
                    .message("카드 결제 처리 중 오류 발생")
                    .build();
        }
    }
    
    private PaymentResponseDTO processBankTransferPayment(Payment payment, PaymentRequestDTO request) {
        // 무통장입금 처리 로직
        return PaymentResponseDTO.builder()
                .paymentStatus(PaymentStatus.PENDING)
                .message("무통장입금 계좌 정보를 확인하세요.")
                .build();
    }
    
    
    private PaymentResponseDTO processPointPayment(Payment payment, PaymentRequestDTO request) {
        try {
            // 포인트 결제 처리
            Order order = payment.getOrder();
            int requiredPoints = payment.getAmount();
            int availablePoints = order.getMember().getAvailablePoints();
            
            if (availablePoints < requiredPoints) {
                return PaymentResponseDTO.builder()
                        .paymentStatus(PaymentStatus.FAILED)
                        .failureReason("포인트가 부족합니다.")
                        .message("포인트가 부족합니다.")
                        .build();
            }
            
            // 포인트 차감
            order.getMember().usePoints(requiredPoints);
            
            return PaymentResponseDTO.builder()
                    .paymentStatus(PaymentStatus.PAID)
                    .pgTransactionId("POINT_" + System.currentTimeMillis())
                    .pgApprovalNumber("POINT_APPROVAL_" + System.currentTimeMillis())
                    .message("포인트 결제가 완료되었습니다.")
                    .build();
                    
        } catch (Exception e) {
            log.error("포인트 결제 처리 실패", e);
            return PaymentResponseDTO.builder()
                    .paymentStatus(PaymentStatus.FAILED)
                    .failureReason(e.getMessage())
                    .message("포인트 결제 처리 중 오류 발생")
                    .build();
        }
    }
    
    @Override
    public boolean handlePaymentCallback(PaymentCallbackDTO callback) {
        try {
            log.info("결제 콜백 처리 시작: transactionId={}, status={}", 
                    callback.getTransactionId(), callback.getStatus());
            
            // 서명 검증
            if (!pgApiService.verifySignature(callback)) {
                log.error("서명 검증 실패: transactionId={}", callback.getTransactionId());
                return false;
            }
            
            // 결제 정보 조회
            Payment payment = paymentRepository.findByPgTransactionId(callback.getTransactionId())
                    .orElseThrow(() -> new PaymentNotFoundException("결제 정보를 찾을 수 없습니다."));
            
            // 결제 상태 업데이트
            switch (callback.getStatus()) {
                case "success":
                    payment.updateStatus(PaymentStatus.PAID);
                    payment.setPgResponse(callback.getTransactionId(), callback.getApprovalNumber(), callback.getRawData());
                    
                    // 주문 상태 업데이트
                    orderService.confirmOrder(payment.getOrder().getOrderId());
                    break;
                    
                case "fail":
                    payment.updateStatus(PaymentStatus.FAILED);
                    payment.setFailureReason("PG사 결제 실패");
                    break;
                    
                case "cancel":
                    payment.updateStatus(PaymentStatus.CANCELLED);
                    break;
                    
                default:
                    log.warn("알 수 없는 결제 상태: {}", callback.getStatus());
                    return false;
            }
            
            paymentRepository.save(payment);
            log.info("결제 콜백 처리 완료: paymentId={}, status={}", payment.getId(), payment.getPaymentStatus());
            return true;
            
        } catch (Exception e) {
            log.error("결제 콜백 처리 실패: transactionId={}", callback.getTransactionId(), e);
            return false;
        }
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
                
                // 주문 취소
                orderService.removeOrder(payment.getOrder().getOrderId(), null);
                
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
            
            if (amount > payment.getAmount()) {
                throw new PaymentException("환불 금액이 결제 금액을 초과할 수 없습니다.");
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
                log.error("PG사 환불 실패: {}", pgResponse.getErrorMessage());
                return false;
            }
            
        } catch (Exception e) {
            log.error("결제 환불 실패: paymentId={}", paymentId, e);
            return false;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentStatus(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        
        return PaymentResponseDTO.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrder().getOrderId())
                .paymentStatus(payment.getPaymentStatus())
                .pgTransactionId(payment.getPgTransactionId())
                .pgApprovalNumber(payment.getPgApprovalNumber())
                .amount(payment.getAmount())
                .message("결제 상태 조회 완료")
                .build();
    }
}
