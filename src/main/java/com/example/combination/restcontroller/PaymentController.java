package com.example.combination.restcontroller;

import com.example.combination.dto.*;
import com.example.combination.service.PaymentService;
import com.example.combination.web.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;
    
    /**
     * 결제 요청
     */
    @PostMapping("/request")
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> requestPayment(
            @Valid @RequestBody PaymentRequestDTO paymentRequest) {
        
        try {
            log.info("결제 요청 API 호출: orderId={}, paymentMethod={}", 
                    paymentRequest.getOrderId(), paymentRequest.getPaymentMethod());
            
            PaymentResponseDTO response = paymentService.processPayment(paymentRequest);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    "결제 요청이 처리되었습니다.", 
                    null, 
                    response));
                    
        } catch (Exception e) {
            log.error("결제 요청 처리 실패", e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "결제 요청 처리 중 오류가 발생했습니다.", 
                    e.getMessage(), 
                    null));
        }
    }
    
    /**
     * PG사 결제 콜백 처리
     */
    @PostMapping("/callback")
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleCallback(
            @RequestBody PaymentCallbackDTO callback) {
        
        try {
            log.info("결제 콜백 API 호출: transactionId={}, status={}", 
                    callback.getTransactionId(), callback.getStatus());
            
            boolean success = paymentService.handlePaymentCallback(callback);
            
            if (success) {
                return ResponseEntity.ok(new ApiResponse<>(
                        "결제 콜백이 성공적으로 처리되었습니다.", 
                        null, 
                        Map.of("success", true)));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse<>(
                        "결제 콜백 처리에 실패했습니다.", 
                        "콜백 데이터 검증 실패", 
                        Map.of("success", false)));
            }
            
        } catch (Exception e) {
            log.error("결제 콜백 처리 실패", e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "결제 콜백 처리 중 오류가 발생했습니다.", 
                    e.getMessage(), 
                    Map.of("success", false)));
        }
    }
    
    /**
     * 결제 취소
     */
    @PostMapping("/cancel/{paymentId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> cancelPayment(
            @PathVariable Long paymentId,
            @RequestParam(required = false, defaultValue = "사용자 요청") String reason) {
        
        try {
            log.info("결제 취소 API 호출: paymentId={}, reason={}", paymentId, reason);
            
            boolean success = paymentService.cancelPayment(paymentId, reason);
            
            if (success) {
                return ResponseEntity.ok(new ApiResponse<>(
                        "결제가 성공적으로 취소되었습니다.", 
                        null, 
                        Map.of("success", true)));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse<>(
                        "결제 취소에 실패했습니다.", 
                        "취소 처리 중 오류 발생", 
                        Map.of("success", false)));
            }
            
        } catch (Exception e) {
            log.error("결제 취소 실패: paymentId={}", paymentId, e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "결제 취소 중 오류가 발생했습니다.", 
                    e.getMessage(), 
                    Map.of("success", false)));
        }
    }
    
    /**
     * 결제 환불
     */
    @PostMapping("/refund/{paymentId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> refundPayment(
            @PathVariable Long paymentId,
            @RequestParam int amount,
            @RequestParam(required = false, defaultValue = "사용자 요청") String reason) {
        
        try {
            log.info("결제 환불 API 호출: paymentId={}, amount={}, reason={}", 
                    paymentId, amount, reason);
            
            boolean success = paymentService.refundPayment(paymentId, amount, reason);
            
            if (success) {
                return ResponseEntity.ok(new ApiResponse<>(
                        "결제가 성공적으로 환불되었습니다.", 
                        null, 
                        Map.of("success", true)));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse<>(
                        "결제 환불에 실패했습니다.", 
                        "환불 처리 중 오류 발생", 
                        Map.of("success", false)));
            }
            
        } catch (Exception e) {
            log.error("결제 환불 실패: paymentId={}", paymentId, e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "결제 환불 중 오류가 발생했습니다.", 
                    e.getMessage(), 
                    Map.of("success", false)));
        }
    }
    
    /**
     * 결제 상태 조회
     */
    @GetMapping("/status/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> getPaymentStatus(
            @PathVariable Long paymentId) {
        
        try {
            log.info("결제 상태 조회 API 호출: paymentId={}", paymentId);
            
            PaymentResponseDTO response = paymentService.getPaymentStatus(paymentId);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    "결제 상태 조회가 완료되었습니다.", 
                    null, 
                    response));
                    
        } catch (Exception e) {
            log.error("결제 상태 조회 실패: paymentId={}", paymentId, e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "결제 상태 조회 중 오류가 발생했습니다.", 
                    e.getMessage(), 
                    null));
        }
    }
    
    /**
     * 결제 성공 페이지 (PG사 리다이렉트)
     */
    @GetMapping("/success")
    public ResponseEntity<ApiResponse<Map<String, Object>>> paymentSuccess(
            @RequestParam(required = false) String paymentKey,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String amount) {
        
        try {
            log.info("결제 성공 페이지 접근: paymentKey={}, orderId={}, amount={}", 
                    paymentKey, orderId, amount);
            
            // 실제 구현에서는 paymentKey를 사용하여 결제 승인 처리
            // paymentService.approvePayment(paymentKey, orderId);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    "결제가 성공적으로 완료되었습니다.", 
                    null, 
                    Map.of(
                        "success", true,
                        "paymentKey", paymentKey,
                        "orderId", orderId,
                        "amount", amount
                    )));
                    
        } catch (Exception e) {
            log.error("결제 성공 페이지 처리 실패", e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "결제 성공 처리 중 오류가 발생했습니다.", 
                    e.getMessage(), 
                    Map.of("success", false)));
        }
    }
    
    /**
     * 결제 실패 페이지 (PG사 리다이렉트)
     */
    @GetMapping("/fail")
    public ResponseEntity<ApiResponse<Map<String, Object>>> paymentFail(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String orderId) {
        
        try {
            log.info("결제 실패 페이지 접근: code={}, message={}, orderId={}", 
                    code, message, orderId);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    "결제가 실패했습니다.", 
                    message, 
                    Map.of(
                        "success", false,
                        "errorCode", code,
                        "errorMessage", message,
                        "orderId", orderId
                    )));
                    
        } catch (Exception e) {
            log.error("결제 실패 페이지 처리 실패", e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "결제 실패 처리 중 오류가 발생했습니다.", 
                    e.getMessage(), 
                    Map.of("success", false)));
        }
    }
    
    /**
     * 결제 취소 페이지 (PG사 리다이렉트)
     */
    @GetMapping("/cancel")
    public ResponseEntity<ApiResponse<Map<String, Object>>> paymentCancel(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String orderId) {
        
        try {
            log.info("결제 취소 페이지 접근: code={}, message={}, orderId={}", 
                    code, message, orderId);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    "결제가 취소되었습니다.", 
                    message, 
                    Map.of(
                        "success", false,
                        "errorCode", code,
                        "errorMessage", message,
                        "orderId", orderId
                    )));
                    
        } catch (Exception e) {
            log.error("결제 취소 페이지 처리 실패", e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "결제 취소 처리 중 오류가 발생했습니다.", 
                    e.getMessage(), 
                    Map.of("success", false)));
        }
    }
}
