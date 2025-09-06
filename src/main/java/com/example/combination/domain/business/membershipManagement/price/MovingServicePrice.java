package com.example.combination.domain.business.membershipManagement.price;

import com.example.combination.domain.member.MembershipGrade;
import com.example.combination.domain.order.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MovingServicePrice {

    private int movingServicePrice;

    private MembershipGrade membershipGrade;

    private ShoppingCart shoppingCart;


}
