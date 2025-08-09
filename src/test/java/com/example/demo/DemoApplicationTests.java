package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import software.amazon.awssdk.services.s3.S3Client;

@SpringBootTest
class DemoApplicationTests {

    @MockBean
    private S3Client s3Client;

	@Test
	void contextLoads() {
	}

}
