package com.example.combination.domain.tracking;

import com.example.combination.domain.order.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 배송 추적 정보를 관리하는 엔티티
 * 실시간 배송 상태 추적 및 시각화를 위한 데이터
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "delivery_trackings")
public class DeliveryTracking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    
    @Column(nullable = false, length = 50)
    private String trackingNumber; // 운송장 번호
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrackingStatus status; // 현재 배송 상태
    
    @Column(length = 200)
    private String currentLocation; // 현재 위치
    
    @Column(length = 200)
    private String nextLocation; // 다음 예정 위치
    
    @Column
    private LocalDateTime estimatedDeliveryTime; // 예상 배송 시간
    
    @Column
    private LocalDateTime actualDeliveryTime; // 실제 배송 완료 시간
    
    @Column(length = 100)
    private String carrier; // 택배사 (CJ대한통운, 한진택배 등)
    
    @Column(length = 500)
    private String notes; // 특이사항
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime updatedAt;
    
    // 배송 상태 업데이트
    public void updateStatus(TrackingStatus newStatus, String location, String notes) {
        this.status = newStatus;
        this.currentLocation = location;
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
        
        if (newStatus == TrackingStatus.DELIVERED) {
            this.actualDeliveryTime = LocalDateTime.now();
        }
    }
    
    // 예상 배송 시간 설정
    public void setEstimatedDeliveryTime(LocalDateTime estimatedTime) {
        this.estimatedDeliveryTime = estimatedTime;
        this.updatedAt = LocalDateTime.now();
    }
    
    // 배송 추적 정보 생성
    public static DeliveryTracking createTracking(Order order, String trackingNumber, String carrier) {
        return DeliveryTracking.builder()
                .order(order)
                .trackingNumber(trackingNumber)
                .status(TrackingStatus.PREPARING)
                .carrier(carrier)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
