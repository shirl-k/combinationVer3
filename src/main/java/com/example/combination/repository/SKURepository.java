package com.example.combination.repository;

import com.example.combination.domain.item.SKU;
import com.example.combination.domain.order.CartItem;
import com.example.combination.dto.CartItemRequestDTO;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SKURepository {
    private final EntityManager em;

    public void save(CartItem cartItem) {
        em.persist(cartItem);
    }
    //장바구니에 있는 상품 SKU조회
    public Optional<CartItem> findByCartIdAndSkuId(Long CartId, String skuId) {
        List<CartItem> result = em.createQuery("select i from CartItem i where i.shoppingCart.cartId =:cartId and i.sku.skuId =:skuId", CartItem.class)
                .setParameter("cartId", CartId)
                .setParameter("skuId", skuId)
                .getResultList();
        return result.stream().findFirst();
    }
    //클라이언트에서 상품 추가 요청 들어왔을 때 요청 상품(SKU) 조회 (장바구니에 상품 추가할 때 조회)
    //PK : skuId
    public Optional<SKU> findBySkuId(String skuId) {
        return Optional.ofNullable(em.find(SKU.class, skuId));
    }

}
/*
  public Optional<SKU> findById(String skuId) {
        List<SKU> result = em.createQuery("select s from SKU s where s.skuId = :skuId", SKU.class)
                .setParameter("skuId", skuId)
                .getResultList();
        return result.stream().findFirst();
    }
 */