package com.example.ivi.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * Text to image generation request DTO
 */
@Data
public class TextToImageRequest {
    
    /**
     * Text prompt for image generation
     */
    @NotBlank(message = "Prompt cannot be empty")
    private String prompt;
    
    /**
     * Image size (optional, default: "1024*1024")
     */
    private String size = "1024*1024";
    
    /**
     * Number of images to generate (optional, default: 1)
     */
    private Integer n = 1;
    
    /**
     * Image quality (optional, default: "standard")
     */
    private String quality = "standard";
    
    /**
     * Image style (optional, default: "vivid")
     */
    private String style = "vivid";
}