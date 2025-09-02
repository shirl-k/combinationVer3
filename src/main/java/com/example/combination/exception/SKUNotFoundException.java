package com.example.combination.exception;

public class SKUNotFoundException extends RuntimeException {
    public SKUNotFoundException(String skuId) {
        super("해당 SKU를 찾을 수 없습니다. id=" + skuId);
    }
}
