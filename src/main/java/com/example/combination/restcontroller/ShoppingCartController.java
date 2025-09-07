package com.example.combination.restcontroller;


import com.example.combination.dto.CartItemDTO;
import com.example.combination.dto.CartItemRequestDTO;
import com.example.combination.service.ShoppingCartService;
import com.example.combination.web.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cart")
@RequiredArgsConstructor

public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;


    //장바구니에서 상품 목록 조회
    @GetMapping("/api/cart/{cartId}/items")
    public ResponseEntity<List<CartItemDTO>> showCartItems(@PathVariable Long cartId) {
        List<CartItemDTO> items = shoppingCartService.findCartItems(cartId);
        return ResponseEntity.ok(items);
    }
    //장바구니에 상품 추가 요청
    @PostMapping("/api/cart/{cartId}/items")
    public ResponseEntity<ApiResponse<Void>> addItemToCart(@PathVariable Long cartId,
                                                     @RequestBody CartItemRequestDTO dto) {//클라이언트가 CartItemRequestDTO를 JSON형태로 보내면 @RequestBody가 DTO로 변환
        shoppingCartService.addItem(cartId, dto);
        ApiResponse<Void> response = new ApiResponse<>("장바구니에 추가되었습니다", null, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); //201 created
    }
    //장바구니에서 상품 삭제 요청
    @DeleteMapping("/api/cart/{cartId}/items/{skuId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long cartId,
                                                   @PathVariable String skuId) {
        shoppingCartService.removeItem(cartId,skuId);
        return ResponseEntity.noContent().build();
    }

}
