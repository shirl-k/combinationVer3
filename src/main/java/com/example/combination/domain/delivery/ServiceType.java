package com.example.combination.domain.delivery;

public enum ServiceType {
    JUST_DELIVERY {
        @Override  //Enum 클래스 내에 선언한 abstract 메서드 오버라이드
        public int calculate(int itemsTotal, int memberDiscount, int MovingServicePrice) {
            return Math.max(0, itemsTotal - memberDiscount);
        }
    },
    MOVING_SERVICE {
        @Override
        public int calculate(int itemsTotal, int memberDiscount, int MovingServicePrice) {
            return Math.max(0, itemsTotal - memberDiscount + MovingServicePrice);
        }
    };
   
    public abstract int calculate(int itemsTotal, int memberDiscount, int MovingServicePrice);
}
