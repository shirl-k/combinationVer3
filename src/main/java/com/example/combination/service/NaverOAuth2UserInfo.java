package com.example.combination.service;

import java.util.Map;

public class NaverOAuth2UserInfo implements OAuth2UserInfo {
    
    private final Map<String, Object> attributes;

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getId() {
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response == null) {
            return null;
        }
        Object id = response.get("id");
        return id != null ? (String) id : null;
    }

    @Override
    public String getName() {
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response == null) {
            return null;
        }
        Object name = response.get("name");
        return name != null ? (String) name : null;
    }

    @Override
    public String getEmail() {
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response == null) {
            return null;
        }
        Object email = response.get("email");
        return email != null ? (String) email : null;
    }

    @Override
    public String getImageUrl() {
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response == null) {
            return null;
        }
        Object profileImage = response.get("profile_image");
        return profileImage != null ? (String) profileImage : null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
