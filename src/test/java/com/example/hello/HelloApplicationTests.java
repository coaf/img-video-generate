package com.example.hello;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfiguration.class)
class HelloApplicationTests {

    @Test
    void contextLoads() {
        // Test that the application context loads successfully without database dependencies
    }

}
