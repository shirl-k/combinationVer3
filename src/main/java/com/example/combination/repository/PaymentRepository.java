package com.example.combination.repository;

import com.example.combination.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findById(Long paymentId);

    Optional<Payment> findByOrderOrderId(Long orderId); //메서드 사용 안됨 findByOrderId -> findByOrderOrderId(스프링 데이터 JPA는 Payment.order.orderId를 찾아서 쿼리를 자동 생성합니다.)
    
    Optional<Payment> findByPgTransactionId(String pgTransactionId);
    
    boolean existsByPgTransactionId(String pgTransactionId);
}
