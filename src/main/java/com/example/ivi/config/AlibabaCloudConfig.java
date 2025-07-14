package com.example.ivi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Alibaba Cloud configuration properties
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "alibaba.cloud")
public class AlibabaCloudConfig {
    
    /**
     * Alibaba Cloud API key
     */
    private String apiKey;
    
    /**
     * Base URL for Alibaba Cloud API
     */
    private String baseUrl;
    
    /**
     * Text to image model configuration
     */
    private TextToImage textToImage = new TextToImage();
    
    /**
     * Text to video model configuration
     */
    private TextToVideo textToVideo = new TextToVideo();
    
    @Data
    public static class TextToImage {
        private String model;
    }
    
    @Data
    public static class TextToVideo {
        private String model;
    }
}