package com.example.combination.exception;

/**
 * 재고 부족 예외
 * 상품 재고가 요청 수량보다 부족할 때 발생
 */
public class OutOfStockException extends BusinessException {
    
    private final String skuId;
    private final int requestedQuantity;
    private final int availableQuantity;
    
    public OutOfStockException(String skuId, int requestedQuantity, int availableQuantity) {
        super(String.format("재고 부족: SKU=%s, 요청수량=%d, 재고수량=%d", 
              skuId, requestedQuantity, availableQuantity));
        this.skuId = skuId;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }
    
    public String getSkuId() {
        return skuId;
    }
    
    public int getRequestedQuantity() {
        return requestedQuantity;
    }
    
    public int getAvailableQuantity() {
        return availableQuantity;
    }
}
