package com.example.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * Test configuration that excludes database-related components
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(
    basePackages = "com.example.hello",
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.example\\.hello\\.mapper\\..*"),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.example\\.hello\\.config\\.MyBatisPlusConfig")
    }
)
public class TestConfiguration {
    
    public static void main(String[] args) {
        SpringApplication.run(TestConfiguration.class, args);
    }
}