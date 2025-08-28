package com.example.combination.repository;


import com.example.combination.domain.member.Member;
import com.example.combination.domain.order.Order;
import com.example.combination.domain.order.OrderStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public void delete(Order order) {
        em.remove(order);
    }

    public Optional<Order> findById(Long orderId) {
        return Optional.ofNullable(em.find(Order.class, orderId)); //NullPointException 예방 Optional
    }

    public List<Order> findByMember(Member member) {
        return em.createQuery("select o from Order o where o.member = :member", Order.class)
                .setParameter("member", member)
                .getResultList();

    }

}
