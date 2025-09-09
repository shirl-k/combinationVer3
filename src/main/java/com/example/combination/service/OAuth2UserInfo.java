package com.example.combination.service;

import java.util.Map;

public interface OAuth2UserInfo {
    String getId();
    String getName();
    String getEmail();
    String getImageUrl();
    Map<String, Object> getAttributes();
}
