package com.example.combination.restcontroller;

import com.example.combination.service.CustomOAuth2User;
import com.example.combination.web.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentUser(
            @AuthenticationPrincipal CustomOAuth2User user) {
        
        if (user == null) {
            return ResponseEntity.ok(new ApiResponse<Map<String, Object>>("로그인이 필요합니다.", null, null));
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("memberId", user.getMemberId());
        userInfo.put("name", user.getMemberName());
        userInfo.put("userId", user.getName());
        userInfo.put("isLoggedIn", true);

        return ResponseEntity.ok(new ApiResponse<Map<String, Object>>("사용자 정보 조회 성공", null, userInfo));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok(new ApiResponse<Void>("로그아웃 성공", null, null));
    }

    @GetMapping("/login-status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getLoginStatus(
            @AuthenticationPrincipal CustomOAuth2User user) {
        
        Map<String, Object> status = new HashMap<>();
        status.put("isLoggedIn", user != null);
        
        if (user != null) {
            status.put("memberId", user.getMemberId());
            status.put("name", user.getMemberName());
        }

        return ResponseEntity.ok(new ApiResponse<Map<String, Object>>("로그인 상태 조회 성공", null, status));
    }
}
