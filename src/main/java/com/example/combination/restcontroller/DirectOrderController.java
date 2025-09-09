package com.example.combination.restcontroller;

import com.example.combination.dto.DirectOrderRequestDTO;
import com.example.combination.dto.DirectOrderResponseDTO;
import com.example.combination.service.DirectOrderService;
import com.example.combination.web.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/direct-order")
@RequiredArgsConstructor
public class DirectOrderController {
    
    private final DirectOrderService directOrderService;
    
    /**
     * 바로 결제 처리
     */
    @PostMapping("/process")
    public ResponseEntity<ApiResponse<DirectOrderResponseDTO>> processDirectOrder(
            @Valid @RequestBody DirectOrderRequestDTO request) {
        
        try {
            log.info("바로 결제 API 호출: skuId={}, quantity={}, serviceType={}, paymentMethod={}", 
                    request.getSkuId(), request.getQuantity(), request.getServiceType(), request.getPaymentMethod());
            
            DirectOrderResponseDTO response = directOrderService.processDirectOrder(request);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(new ApiResponse<>(
                        "바로 결제가 성공적으로 처리되었습니다.", 
                        null, 
                        response));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse<>(
                        "바로 결제 처리에 실패했습니다.", 
                        response.getErrorMessage(), 
                        response));
            }
            
        } catch (Exception e) {
            log.error("바로 결제 처리 실패", e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "바로 결제 처리 중 오류가 발생했습니다.", 
                    e.getMessage(), 
                    null));
        }
    }
    
    /**
     * 바로 결제 정보 조회 (테스트용)
     */
    @GetMapping("/info/{skuId}")
    public ResponseEntity<ApiResponse<Object>> getDirectOrderInfo(@PathVariable String skuId) {
        try {
            log.info("바로 결제 정보 조회 API 호출: skuId={}", skuId);
            
            // 실제 구현에서는 SKU 정보를 조회하여 반환
            Object skuInfo = java.util.Map.of(
                "skuId", skuId,
                "itemName", "테스트 상품",
                "unitPrice", 100000,
                "availableServices", java.util.List.of("JUST_DELIVERY", "MOVING_SERVICE"),
                "movingServicePrice", 500000
            );
            
            return ResponseEntity.ok(new ApiResponse<>(
                    "바로 결제 정보 조회가 완료되었습니다.", 
                    null, 
                    skuInfo));
                    
        } catch (Exception e) {
            log.error("바로 결제 정보 조회 실패: skuId={}", skuId, e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "바로 결제 정보 조회 중 오류가 발생했습니다.", 
                    e.getMessage(), 
                    null));
        }
    }
}
