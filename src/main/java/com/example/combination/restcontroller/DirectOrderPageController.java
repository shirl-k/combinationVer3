package com.example.combination.restcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/direct-order")
public class DirectOrderPageController {
    
    /**
     * 바로 결제 페이지 표시
     */
    @GetMapping("/{skuId}")
    public String directOrderPage(@PathVariable String skuId, Model model) {
        model.addAttribute("skuId", skuId);
        return "direct-order";
    }
    
    /**
     * 바로 결제 완료 페이지
     */
    @GetMapping("/complete/{orderId}")
    public String directOrderCompletePage(@PathVariable Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "direct-order-complete";
    }
}
