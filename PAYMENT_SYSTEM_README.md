# 결제 시스템 사용 가이드

## 개요
인테리어 쇼핑몰의 PG사 연동 결제 시스템입니다. 토스페이먼츠, 이니시스, KCP 등 다양한 PG사를 지원합니다.

## 주요 기능
- 카드 결제
- 무통장입금
- 포인트 결제
- 결제 취소
- 환불 처리
- 결제 상태 조회

## API 엔드포인트

### 1. 결제 요청
```
POST /api/payment/request
Content-Type: application/json

{
    "orderId": 12345,
    "paymentMethod": "CARD",
    "usePoints": false
}
```

### 2. 결제 콜백 처리
```
POST /api/payment/callback
Content-Type: application/json

{
    "transactionId": "payment_key_123",
    "approvalNumber": "approval_456",
    "status": "success",
    "amount": 50000,
    "paymentMethod": "CARD",
    "signature": "signature_hash",
    "rawData": "original_callback_data"
}
```

### 3. 결제 취소
```
POST /api/payment/cancel/{paymentId}?reason=사용자요청
```

### 4. 결제 환불
```
POST /api/payment/refund/{paymentId}?amount=50000&reason=상품불량
```

### 5. 결제 상태 조회
```
GET /api/payment/status/{paymentId}
```

## 환경 설정

### application.yml 설정
```yaml
# PG사 설정
pg:
  toss:
    secret-key: ${TOSS_SECRET_KEY:test_sk_}
    base-url: ${TOSS_BASE_URL:https://api.tosspayments.com}
  inicis:
    mid: ${INICIS_MID:your-inicis-mid}
    api-key: ${INICIS_API_KEY:your-inicis-api-key}
    base-url: ${INICIS_BASE_URL:https://api.inicis.com}

# 결제 관련 설정
payment:
  success-url: ${PAYMENT_SUCCESS_URL:http://localhost:8080/api/payment/success}
  fail-url: ${PAYMENT_FAIL_URL:http://localhost:8080/api/payment/fail}
  cancel-url: ${PAYMENT_CANCEL_URL:http://localhost:8080/api/payment/cancel}
  callback-url: ${PAYMENT_CALLBACK_URL:http://localhost:8080/api/payment/callback}
```

### 환경 변수 설정
```bash
# 토스페이먼츠
export TOSS_SECRET_KEY=test_sk_your_secret_key
export TOSS_BASE_URL=https://api.tosspayments.com

# 이니시스
export INICIS_MID=your_inicis_mid
export INICIS_API_KEY=your_inicis_api_key
export INICIS_BASE_URL=https://api.inicis.com
```

## 사용 예시

### 1. 결제 페이지 접근
```
GET /payment/page/12345
```

### 2. JavaScript로 결제 요청
```javascript
const paymentData = {
    orderId: 12345,
    paymentMethod: "CARD",
    usePoints: false
};

fetch('/api/payment/request', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify(paymentData)
})
.then(response => response.json())
.then(data => {
    if (data.data.redirectUrl) {
        window.location.href = data.data.redirectUrl;
    }
});
```

## 결제 흐름

1. **결제 요청**: 클라이언트에서 결제 요청
2. **PG사 연동**: 선택된 PG사로 결제 요청 전송
3. **결제 페이지**: PG사 결제 페이지로 리다이렉트
4. **결제 완료**: PG사에서 결제 완료 후 콜백 수신
5. **주문 확정**: 결제 완료 후 주문 상태 업데이트

## 지원하는 PG사

### 토스페이먼츠
- 카드 결제
- 가상계좌
- 계좌이체
- 간편결제

### 이니시스
- 카드 결제
- 무통장입금
- 휴대폰 결제

### KCP
- 카드 결제
- 계좌이체
- 가상계좌

## 보안 고려사항

1. **서명 검증**: PG사 콜백 데이터의 서명을 반드시 검증
2. **HTTPS 사용**: 모든 결제 관련 통신은 HTTPS 사용
3. **환경 변수**: 민감한 정보는 환경 변수로 관리
4. **로깅**: 결제 관련 모든 로그를 기록하되, 민감 정보는 마스킹

## 에러 처리

### 주요 예외 클래스
- `PaymentException`: 일반적인 결제 오류
- `PaymentNotFoundException`: 결제 정보를 찾을 수 없음
- `PgApiException`: PG사 API 호출 오류

### 에러 응답 형식
```json
{
    "success": false,
    "message": "결제 처리 중 오류가 발생했습니다.",
    "error": "구체적인 오류 메시지",
    "data": null
}
```

## 테스트

### 테스트 환경 설정
```yaml
pg:
  toss:
    secret-key: test_sk_
    base-url: https://api.tosspayments.com
```

### 테스트 카드 번호 (토스페이먼츠)
- 성공: 4242424242424242
- 실패: 4000000000000002
- 취소: 4000000000000069

## 모니터링

### 로그 레벨 설정
```yaml
logging.level:
  com.example.combination.service.impl.PaymentServiceImpl: DEBUG
  com.example.combination.service.impl.TossPaymentServiceImpl: DEBUG
```

### 주요 모니터링 포인트
- 결제 성공률
- 결제 응답 시간
- PG사 API 오류율
- 결제 취소/환불 비율

## 확장 가능성

### 새로운 PG사 추가
1. `PgApiService` 인터페이스 구현
2. `PaymentServiceImpl`에서 새로운 PG사 처리 로직 추가
3. `application.yml`에 설정 추가

### 새로운 결제 방식 추가
1. `PaymentMethod` enum에 새로운 값 추가
2. `PaymentServiceImpl`에 처리 메서드 추가
3. 프론트엔드 UI 업데이트

## 문의사항
결제 시스템 관련 문의사항이 있으시면 개발팀에 연락해주세요.
