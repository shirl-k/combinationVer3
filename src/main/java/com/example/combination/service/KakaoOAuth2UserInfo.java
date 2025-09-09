package com.example.combination.service;

import java.util.Map;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {
    
    private final Map<String, Object> attributes;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getName() {
        @SuppressWarnings("unchecked")
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        return (String) properties.get("nickname");
    }

    @Override
    public String getEmail() {
        @SuppressWarnings("unchecked")
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getImageUrl() {
        @SuppressWarnings("unchecked")
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        return (String) properties.get("profile_image");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
