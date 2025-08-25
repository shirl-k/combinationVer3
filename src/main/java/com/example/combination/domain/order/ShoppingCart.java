package com.example.combination.domain.order;

import com.example.combination.domain.member.Member;
import com.example.combination.repository.ItemCount;
import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor//매개변수 없는 생성자
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "shoppingCart")
public class ShoppingCart implements ItemCount {
    @Id @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "shoppingCart",cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();



    //------------//




//    private TotalPrice totalPrice;
//    private int discountPrice;

    // setter 대신 안전하게 연관관계 편의 메서드 사용
//    public void setMember(Member member) {
//        this.member = member;
//    }

/*    //장바구니 자동생성 연관관계 편의 메서드 사용 예시
    Member member = new Member();

   // 멤버 생성 시 자동으로 장바구니도 같이 생성
    member.createShoppingCart();

    entityManager.persist(member);
// cascade = CascadeType.ALL 때문에 ShoppingCart도 자동 저장됨

 */


}
