package com.example.ivi;

import javax.swing.text.IconView;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Main application class for Image and Video Generation Service
 */
@SpringBootApplication
@EnableConfigurationProperties
@MapperScan("com.example.ivi.mapper")
public class IviApplication {

    public static void main(String[] args) {
        SpringApplication.run(IviApplication.class, args);
    }

}
