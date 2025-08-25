package com.example.combination.repository;

import com.example.combination.domain.order.OrderItem;
import jakarta.persistence.*;
import lombok.*;

@Getter
@RequiredArgsConstructor//매개변수 없는 생성자
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "StockItem")
public class StockItem implements ItemCount{

    @Id @GeneratedValue
    private int id;
    private int stockQuantity;
    private int canceledQuantity;
    private int orderedQuantity;

    @Override
    public int addItem() {
        return stockQuantity + canceledQuantity;
    }

    @Override
    public int removeItem() {
        return stockQuantity - orderedQuantity;
    }
}
