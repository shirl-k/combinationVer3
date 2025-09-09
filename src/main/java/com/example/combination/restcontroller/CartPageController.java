package com.example.combination.restcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartPageController {
    
    /**
     * 장바구니 페이지 표시
     */
    @GetMapping("/{cartId}")
    public String cartPage(@PathVariable Long cartId, Model model) {
        model.addAttribute("cartId", cartId);
        return "cart";
    }
    
    /**
     * 기본 장바구니 페이지 (임시 cartId 사용)
     */
    @GetMapping
    public String defaultCartPage(Model model) {
        // 실제 구현에서는 로그인한 사용자의 기본 장바구니 ID를 가져와야 함
        model.addAttribute("cartId", 1L); // 임시 값
        return "cart";
    }
}
