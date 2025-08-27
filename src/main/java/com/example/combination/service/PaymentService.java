package com.example.combination.service;

public class PaymentService {
    /*
    추천 구조

검증 단계 (선택적)

사용자가 결제 버튼 누르기 전에 → “이 상품 결제 가능한가?” 확인

이때는 OrderCheck DTO 사용해도 좋음 (단, Optional)

결제 단계 (필수)

트랜잭션 안에서

CartItem → OrderItem 변환

재고 확인 + 차감

결제 승인 → Order 확정


OrderCheck 같은 “검증용 DTO”를 두고,

1단계: 결제 전 사전 검증 (재고 충분 여부 응답)

2단계: 실제 결제 처리 시 한 번 더 재고 차감
이렇게 이중 검증 구조가 더 안전함
     */


}
