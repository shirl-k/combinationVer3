package com.example.combination;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestPaymentConfig {
    @Bean
    public PaymentClient paymentClient() {
        return Mockito.mock(PaymentClient.class);
    }
}
