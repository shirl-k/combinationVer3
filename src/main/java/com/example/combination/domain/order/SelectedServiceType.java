package com.example.combination.domain.order;

import com.example.combination.domain.delivery.ServiceType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SelectedServiceType { //장바구니에서 배송 옵션 선택한 정보

    @Id @GeneratedValue
    private Long Id;

    private Long cartId;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    public int shoppingCartPrice;

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;
}
