package com.example.combination;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({TestConfiguration.class})
class CombinationApplicationTests {

	@MockBean
	private PaymentClient paymentClient;

	@Test
	void contextLoads() {
	}
}
