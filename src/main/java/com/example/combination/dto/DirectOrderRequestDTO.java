package com.example.combination.dto;

import com.example.combination.domain.delivery.ServiceType;
import com.example.combination.domain.payment.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectOrderRequestDTO {
    
    // 상품 정보
    @NotNull(message = "상품 ID는 필수입니다.")
    private Long spuId;
    
    private String itemName;
    
    @NotNull(message = "SKU ID는 필수입니다.")
    private String skuId;
    
    @Positive(message = "수량은 1개 이상이어야 합니다.")
    private int quantity;
    
    @Positive(message = "단가는 0원 이상이어야 합니다.")
    private int unitPrice;
    
    // 서비스 타입
    @NotNull(message = "서비스 타입은 필수입니다.")
    private ServiceType serviceType;
    
    // 결제 정보
    @NotNull(message = "결제 방식은 필수입니다.")
    private PaymentMethod paymentMethod;
    
    @Builder.Default
    private boolean usePoints = false;
    
    // 배송 정보 (이사 서비스인 경우)
    private String deliveryAddress;
    private String deliveryDetailAddress; // 배송 상세 주소
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    
    // 이사 서비스 정보 (MOVING_SERVICE인 경우)
    private String homeAddress;
    private String homeDetailAddress; // 기존 주소 상세 주소
    private String movingDate;
    private String movingDescription;
    private String movingDetailAddress; // 새 주소 상세 주소
}
