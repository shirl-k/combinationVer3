package com.example.combination.domain.category;

import com.example.combination.domain.item.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@NoArgsConstructor//매개변수 없는 생성자
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "categoryItem")
public class CategoryItem {

    @Id @GeneratedValue
    private Long id;

    private Category category;

    private Item item;

}
