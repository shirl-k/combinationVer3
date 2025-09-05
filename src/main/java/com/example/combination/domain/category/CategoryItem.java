package com.example.combination.domain.category;

import com.example.combination.domain.item.Item;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)//매개변수 없는 생성자
@AllArgsConstructor
@Builder
@Entity
@Table(name = "category_item")
public class CategoryItem {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

}
