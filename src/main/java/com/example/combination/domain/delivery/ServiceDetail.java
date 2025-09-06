package com.example.combination.domain.delivery;

import com.example.combination.domain.order.Order;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "service_type", discriminatorType = DiscriminatorType.STRING)
public abstract class ServiceDetail {

    @Id @GeneratedValue
    private Long id;

    @Column(length = 500)
    private String orderRequestDetail;

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;
}
