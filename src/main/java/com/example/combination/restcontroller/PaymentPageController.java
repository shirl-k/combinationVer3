package com.example.combination.restcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment")
public class PaymentPageController {
    
    /**
     * 결제 페이지 표시
     */
    @GetMapping("/page/{orderId}")
    public String paymentPage(@PathVariable Long orderId, Model model) {
        // 실제 구현에서는 주문 정보를 조회하여 모델에 추가
        model.addAttribute("orderId", orderId);
        model.addAttribute("orderAmount", 50000); // 임시 값
        model.addAttribute("availablePoints", 5000); // 임시 값
        
        return "payment";
    }
}
