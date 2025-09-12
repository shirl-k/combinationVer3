package com.example.combination.service;

import java.util.Map;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo {
    
    private final Map<String, Object> attributes;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getId() {
        Object sub = attributes.get("sub");
        return sub != null ? (String) sub : null;
    }

    @Override
    public String getName() {
        Object name = attributes.get("name");
        return name != null ? (String) name : null;
    }

    @Override
    public String getEmail() {
        Object email = attributes.get("email");
        return email != null ? (String) email : null;
    }

    @Override
    public String getImageUrl() {
        Object picture = attributes.get("picture");
        return picture != null ? (String) picture : null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
