package com.example.combination.service.impl;

import com.example.combination.dto.PgPaymentRequestDTO;
import com.example.combination.dto.PgPaymentResponseDTO;
import com.example.combination.dto.PaymentCallbackDTO;
import com.example.combination.exception.PgApiException;
import com.example.combination.service.PgApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossPaymentServiceImpl implements PgApiService {
    
    @Value("${pg.toss.secret-key}")
    private String secretKey;
    
    @Value("${pg.toss.base-url}")
    private String baseUrl;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    @Override
    public PgPaymentResponseDTO requestPayment(PgPaymentRequestDTO request) {
        try {
            log.info("토스페이먼츠 결제 요청 시작: orderId={}, amount={}", request.getOrderId(), request.getAmount());
            
            // 토스페이먼츠 API 요청 데이터 구성
            Map<String, Object> paymentData = new HashMap<>();
            paymentData.put("orderId", request.getOrderId().toString());
            paymentData.put("orderName", request.getOrderName());
            paymentData.put("amount", request.getAmount());
            paymentData.put("customerName", request.getCustomerName());
            paymentData.put("customerEmail", request.getCustomerEmail());
            paymentData.put("successUrl", request.getSuccessUrl());
            paymentData.put("failUrl", request.getFailUrl());
            
            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Basic " + java.util.Base64.getEncoder()
                    .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(paymentData, headers);
            
            // 토스페이먼츠 API 호출
            String url = baseUrl + "/v1/payments";
            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, 
                    (Class<Map<String, Object>>) (Class<?>) Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                return PgPaymentResponseDTO.builder()
                        .success(true)
                        .transactionId((String) responseBody.get("paymentKey"))
                        .redirectUrl((String) responseBody.get("checkoutUrl"))
                        .rawResponse(objectMapper.writeValueAsString(responseBody))
                        .message("결제 요청 성공")
                        .build();
            } else {
                throw new PgApiException("토스페이먼츠 API 호출 실패: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            log.error("토스페이먼츠 결제 요청 실패", e);
            return PgPaymentResponseDTO.builder()
                    .success(false)
                    .errorMessage(e.getMessage())
                    .message("결제 요청 실패")
                    .build();
        }
    }
    
    @Override
    public PgPaymentResponseDTO approvePayment(String transactionId, String approvalNumber) {
        try {
            log.info("토스페이먼츠 결제 승인 시작: transactionId={}", transactionId);
            
            Map<String, Object> approveData = new HashMap<>();
            approveData.put("orderId", approvalNumber); // 실제로는 주문 ID가 필요
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Basic " + java.util.Base64.getEncoder()
                    .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(approveData, headers);
            
            String url = baseUrl + "/v1/payments/" + transactionId;
            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, 
                    (Class<Map<String, Object>>) (Class<?>) Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                return PgPaymentResponseDTO.builder()
                        .success(true)
                        .transactionId(transactionId)
                        .approvalNumber((String) responseBody.get("approvedAt"))
                        .rawResponse(objectMapper.writeValueAsString(responseBody))
                        .message("결제 승인 성공")
                        .build();
            } else {
                throw new PgApiException("토스페이먼츠 결제 승인 실패: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            log.error("토스페이먼츠 결제 승인 실패", e);
            return PgPaymentResponseDTO.builder()
                    .success(false)
                    .errorMessage(e.getMessage())
                    .message("결제 승인 실패")
                    .build();
        }
    }
    
    @Override
    public PgPaymentResponseDTO cancelPayment(String transactionId, String reason) {
        try {
            log.info("토스페이먼츠 결제 취소 시작: transactionId={}", transactionId);
            
            Map<String, Object> cancelData = new HashMap<>();
            cancelData.put("cancelReason", reason);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Basic " + java.util.Base64.getEncoder()
                    .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(cancelData, headers);
            
            String url = baseUrl + "/v1/payments/" + transactionId + "/cancel";
            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, 
                    (Class<Map<String, Object>>) (Class<?>) Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return PgPaymentResponseDTO.builder()
                        .success(true)
                        .transactionId(transactionId)
                        .message("결제 취소 성공")
                        .build();
            } else {
                throw new PgApiException("토스페이먼츠 결제 취소 실패: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            log.error("토스페이먼츠 결제 취소 실패", e);
            return PgPaymentResponseDTO.builder()
                    .success(false)
                    .errorMessage(e.getMessage())
                    .message("결제 취소 실패")
                    .build();
        }
    }
    
    @Override
    public PgPaymentResponseDTO refundPayment(String transactionId, int amount, String reason) {
        try {
            log.info("토스페이먼츠 환불 시작: transactionId={}, amount={}", transactionId, amount);
            
            Map<String, Object> refundData = new HashMap<>();
            refundData.put("cancelReason", reason);
            refundData.put("cancelAmount", amount);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Basic " + java.util.Base64.getEncoder()
                    .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(refundData, headers);
            
            String url = baseUrl + "/v1/payments/" + transactionId + "/cancel";
            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, 
                    (Class<Map<String, Object>>) (Class<?>) Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return PgPaymentResponseDTO.builder()
                        .success(true)
                        .transactionId(transactionId)
                        .message("환불 성공")
                        .build();
            } else {
                throw new PgApiException("토스페이먼츠 환불 실패: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            log.error("토스페이먼츠 환불 실패", e);
            return PgPaymentResponseDTO.builder()
                    .success(false)
                    .errorMessage(e.getMessage())
                    .message("환불 실패")
                    .build();
        }
    }
    
    @Override
    public boolean verifySignature(PaymentCallbackDTO callback) {
        try {
            // 토스페이먼츠 서명 검증 로직
            // 실제 구현에서는 토스페이먼츠에서 제공하는 서명 검증 방법을 사용
            String expectedSignature = generateSignature(callback);
            return expectedSignature.equals(callback.getSignature());
        } catch (Exception e) {
            log.error("서명 검증 실패", e);
            return false;
        }
    }
    
    private String generateSignature(PaymentCallbackDTO callback) {
        try {
            String data = callback.getTransactionId() + callback.getAmount() + secretKey;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new PgApiException("서명 생성 실패", e);
        }
    }
}
