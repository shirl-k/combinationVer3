package com.example.combination.service;

import com.example.combination.Assembler.CartItemAssembler;
import com.example.combination.domain.order.CartItem;
import com.example.combination.domain.order.ShoppingCart;
import com.example.combination.dto.CartItemDTO;
import com.example.combination.dto.OrderItemDTO;
import com.example.combination.repository.CartItemRepository;
import com.example.combination.repository.ItemRepository;
import com.example.combination.repository.ShoppingCartRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
@Transactional(readOnly = true)
public class ShoppingCartService {


    private final CartItemAssembler cartItemAssembler;
    private final ShoppingCartRepository shoppingCartRepository;


    public List<CartItemDTO> getCartItems(Long cartId) { //클라이언트(Controller)에서 장바구니 상품 목록 조회 요청 시 호출
        ShoppingCart cart =  shoppingCartRepository.findById(cartId) //장바구니 조회
                .orElseThrow(()-> new IllegalStateException("장바구니 없음"));

        return cartItemAssembler.toDTOs(cart.getCartItems()); //CartItemAssembler에서 CartItem 도메인을 DTO 리스트로 변환한 것 가져와서
    } //리스트 내 모든 CartItem을 DTO로 변환

    public void addItem(OrderItemDTO orderItemDTO) {

    }
    //뷰페이지로 다시 반환시 사용

}
