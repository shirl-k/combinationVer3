package com.example.combination.repository;

import com.example.combination.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    Optional<Payment> findByOrderId(Long orderId);
    
    Optional<Payment> findByPgTransactionId(String pgTransactionId);
    
    boolean existsByPgTransactionId(String pgTransactionId);
}
