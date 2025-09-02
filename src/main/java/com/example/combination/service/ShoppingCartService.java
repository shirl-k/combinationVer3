package com.example.combination.service;

import com.example.combination.Assembler.CartItemAssembler;
import com.example.combination.domain.item.SKU;
import com.example.combination.domain.order.CartItem;
import com.example.combination.domain.order.ShoppingCart;
import com.example.combination.dto.CartItemDTO;
import com.example.combination.dto.CartItemRequestDTO;
import com.example.combination.exception.CartNotFoundException;
import com.example.combination.exception.SKUNotFoundException;
import com.example.combination.repository.SKURepository;
import com.example.combination.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingCartService {


    private final CartItemAssembler cartItemAssembler;
    private final ShoppingCartRepository shoppingCartRepository;
    private final SKURepository skuRepository;


    //장바구니 상품 목록 조회
    public List<CartItemDTO> findCartItems(Long cartId) { //클라이언트(Controller)에서 장바구니 상품 목록 조회 요청 시 호출
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        return cartItemAssembler.toDTOs(cart.getCartItems()); //CartItemAssembler에서 CartItem 도메인을 DTO 리스트로 변환한 것 가져와서
    } //리스트 내 모든 CartItem을 DTO로 변환

    //CartItemRequestDTO의 요청 상품 skuId 로 SKU 엔티티에서 조회해서 장바구니에 상품 추가
    @Transactional
    public void addItem(Long cartId, CartItemRequestDTO dto) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        SKU sku = skuRepository.findBySkuId(dto.getSkuId())
                .orElseThrow(() -> new SKUNotFoundException(dto.getSkuId()));

        //CartItemRequestDTO -> CartItem 변환
        CartItem cartItem = CartItem.fromCartItemRequestDTO(dto, sku, cart);

        //연관관계 편의 메서드로 상품 추가 (shoppingCart 엔티티)
        cart.addItemToCart(cartItem);
        shoppingCartRepository.save(cart);

    }

    //상품 삭제 - skuId로 바로 삭제 도메인에서 호출
    public void removeItem(Long cartId, String skuId) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        cart.removeItemBySkuId(skuId);
        shoppingCartRepository.save(cart);
    }

    // 수량 수정  - --->
    public void increaseQuantity(Long cartId,String skuId,int quantity) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));



    }
}
    /*
      public void increaseQuantity(String skuId, int amount) {
        CartItem item = findItemBySkuId(skuId)
                .orElseThrow(()->new SKUNotFoundException("장바구니에 해당 SKU가 없음.: " + skuId));
        item.setQuantity(item.getQuantity() + amount);
    }
     */


// 1. 장바구니 조회
//ShoppingCart cart = cartRepository.findById(cartId).orElseThrow();
//
// 2. CartItem → DTO 변환
//List<CartItemDTO> cartItemDTOs = cartItemAssembler.toDTOs(cart.getCartItems());
//
// 3. DTO → OrderItem 변환 (스냅샷 생성)
//List<OrderItem> orderItems = cartItemDTOs.stream()
//        .map(OrderItem::fromDTO)
//        .toList();
// 4. 주문 생성 시 Order 엔티티에 OrderItem 추가
//Order order = Order.createOrder(member, orderItems);
//orderRepository.save(order);