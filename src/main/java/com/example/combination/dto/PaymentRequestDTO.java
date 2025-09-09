package com.example.combination.dto;

import com.example.combination.domain.payment.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {

    @NotNull(message = "주문 ID는 필수입니다.")
    private Long orderId;
    
    @NotNull(message = "결제 방식은 필수입니다.")
    private PaymentMethod paymentMethod;
    
    @Builder.Default
    private boolean usePoints = false;
}
