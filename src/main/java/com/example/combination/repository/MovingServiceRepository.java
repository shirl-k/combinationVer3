package com.example.combination.repository;

import com.example.combination.domain.movingService.MovingService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MovingServiceRepository {

    public final EntityManager em;

    public void save(MovingService movingService) {
        em.persist(movingService);
    }
    //이사 서비스 - 배송건 ID로 배송 정보 조회
    public Optional<MovingService> findByMovingId(Long id) {
        return Optional.ofNullable(em.find(MovingService.class, id));
    }
    //이사 서비스 전체 배송정보 조회
    public List<MovingService> findAllByMovingId(Long id) {
        return em.createQuery("select ms from MovingService ms",MovingService.class)
                .getResultList();
    }
}
