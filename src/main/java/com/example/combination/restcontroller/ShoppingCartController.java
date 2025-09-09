package com.example.combination.restcontroller;

import com.example.combination.domain.delivery.ServiceType;
import com.example.combination.domain.order.ShoppingCart;
import com.example.combination.dto.CartItemDTO;
import com.example.combination.dto.CartItemRequestDTO;
import com.example.combination.service.ShoppingCartService;
import com.example.combination.web.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    
    private final ShoppingCartService shoppingCartService;
    
    /**
     * 장바구니 상품 목록 조회
     */
    @GetMapping("/{cartId}/items")
    public ResponseEntity<ApiResponse<List<CartItemDTO>>> getCartItems(@PathVariable Long cartId) {
        try {
            log.info("장바구니 상품 목록 조회 API 호출: cartId={}", cartId);
            
            List<CartItemDTO> cartItems = shoppingCartService.findCartItems(cartId);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    "장바구니 상품 목록 조회가 완료되었습니다.", 
                    null, 
                    cartItems));
                    
        } catch (Exception e) {
            log.error("장바구니 상품 목록 조회 실패: cartId={}", cartId, e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "장바구니 상품 목록 조회 중 오류가 발생했습니다.", 
                    e.getMessage(), 
                    null));
        }
    }
    
    /**
     * 장바구니에 상품 추가
     */
    @PostMapping("/{cartId}/items")
    public ResponseEntity<ApiResponse<String>> addItemToCart(
            @PathVariable Long cartId,
            @Valid @RequestBody CartItemRequestDTO cartItemRequest) {
        
        try {
            log.info("장바구니 상품 추가 API 호출: cartId={}, skuId={}", 
                    cartId, cartItemRequest.getSkuId());
            
            shoppingCartService.addItem(cartId, cartItemRequest);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    "상품이 장바구니에 추가되었습니다.", 
                    null, 
                    "success"));
                    
        } catch (Exception e) {
            log.error("장바구니 상품 추가 실패: cartId={}", cartId, e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "장바구니 상품 추가 중 오류가 발생했습니다.", 
                    e.getMessage(), 
                    null));
        }
    }
    
    /**
     * 장바구니에서 상품 삭제
     */
    @DeleteMapping("/{cartId}/items/{skuId}")
    public ResponseEntity<ApiResponse<String>> removeItemFromCart(
            @PathVariable Long cartId,
            @PathVariable String skuId) {
        
        try {
            log.info("장바구니 상품 삭제 API 호출: cartId={}, skuId={}", cartId, skuId);
            
            shoppingCartService.removeItem(cartId, skuId);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    "상품이 장바구니에서 삭제되었습니다.", 
                    null, 
                    "success"));
                    
        } catch (Exception e) {
            log.error("장바구니 상품 삭제 실패: cartId={}, skuId={}", cartId, skuId, e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "장바구니 상품 삭제 중 오류가 발생했습니다.", 
                    e.getMessage(), 
                    null));
        }
    }
    
    /**
     * 상품 수량 증가
     */
    @PutMapping("/{cartId}/items/{skuId}/increase")
    public ResponseEntity<ApiResponse<String>> increaseQuantity(
            @PathVariable Long cartId,
            @PathVariable String skuId,
            @RequestParam(defaultValue = "1") int quantity) {
        
        try {
            log.info("상품 수량 증가 API 호출: cartId={}, skuId={}, quantity={}", 
                    cartId, skuId, quantity);
            
            shoppingCartService.increaseQuantity(cartId, skuId, quantity);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    "상품 수량이 증가되었습니다.", 
                    null, 
                    "success"));
                    
        } catch (Exception e) {
            log.error("상품 수량 증가 실패: cartId={}, skuId={}", cartId, skuId, e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "상품 수량 증가 중 오류가 발생했습니다.", 
                    e.getMessage(), 
                    null));
        }
    }
    
    /**
     * 서비스 타입 설정 (핵심 기능)
     */
    @PutMapping("/{cartId}/service-type")
    public ResponseEntity<ApiResponse<String>> updateServiceType(
            @PathVariable Long cartId,
            @RequestParam ServiceType serviceType) {
        
        try {
            log.info("서비스 타입 설정 API 호출: cartId={}, serviceType={}", cartId, serviceType);
            
            shoppingCartService.updateServiceType(cartId, serviceType);
            
            String message = serviceType == ServiceType.MOVING_SERVICE ? 
                    "이사 서비스가 선택되었습니다. 추가 비용이 적용됩니다." : 
                    "일반 배송 서비스가 선택되었습니다.";
            
            return ResponseEntity.ok(new ApiResponse<>(
                    message, 
                    null, 
                    "success"));
                    
        } catch (Exception e) {
            log.error("서비스 타입 설정 실패: cartId={}, serviceType={}", cartId, serviceType, e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "서비스 타입 설정 중 오류가 발생했습니다.", 
                    e.getMessage(), 
                    null));
        }
    }
    
    /**
     * 총 금액 조회 (핵심 기능)
     */
    @GetMapping("/{cartId}/total")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTotalPrice(@PathVariable Long cartId) {
        try {
            log.info("총 금액 조회 API 호출: cartId={}", cartId);
            
            ShoppingCart cart = shoppingCartService.findById(cartId);
            int totalPrice = shoppingCartService.calculateTotalPrice(cartId);
            
            Map<String, Object> result = Map.of(
                "totalPrice", totalPrice,
                "serviceType", cart.getServiceType() != null ? cart.getServiceType().toString() : "NOT_SELECTED",
                "itemCount", cart.getCartItems().size(),
                "formattedPrice", String.format("%,d원", totalPrice)
            );
            
            return ResponseEntity.ok(new ApiResponse<>(
                    "총 금액 조회가 완료되었습니다.", 
                    null, 
                    result));
                    
        } catch (Exception e) {
            log.error("총 금액 조회 실패: cartId={}", cartId, e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "총 금액 조회 중 오류가 발생했습니다.", 
                    e.getMessage(), 
                    null));
        }
    }
    
    /**
     * 장바구니 정보 조회
     */
    @GetMapping("/{cartId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCartInfo(@PathVariable Long cartId) {
        try {
            log.info("장바구니 정보 조회 API 호출: cartId={}", cartId);
            
            ShoppingCart cart = shoppingCartService.findById(cartId);
            List<CartItemDTO> cartItems = shoppingCartService.findCartItems(cartId);
            
            Map<String, Object> cartInfo = Map.of(
                "cartId", cart.getCartId(),
                "cartName", cart.getCartName(),
                "description", cart.getDescription(),
                "serviceType", cart.getServiceType() != null ? cart.getServiceType().toString() : "NOT_SELECTED",
                "itemCount", cartItems.size(),
                "items", cartItems
            );
            
            return ResponseEntity.ok(new ApiResponse<>(
                    "장바구니 정보 조회가 완료되었습니다.", 
                    null, 
                    cartInfo));
                    
        } catch (Exception e) {
            log.error("장바구니 정보 조회 실패: cartId={}", cartId, e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "장바구니 정보 조회 중 오류가 발생했습니다.", 
                    e.getMessage(), 
                    null));
        }
    }
}
