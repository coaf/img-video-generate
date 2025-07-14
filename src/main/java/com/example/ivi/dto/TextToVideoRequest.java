package com.example.ivi.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * Text to video generation request DTO
 */
@Data
public class TextToVideoRequest {
    
    /**
     * Text prompt for video generation
     */
    @NotBlank(message = "Prompt cannot be empty")
    private String prompt;
    
    /**
     * Video duration in seconds (optional, default: 6)
     */
    private Integer duration = 6;
    
    /**
     * Video aspect ratio (optional, default: "16:9")
     */
    private String aspectRatio = "16:9";
    
    /**
     * Video quality (optional, default: "standard")
     */
    private String quality = "standard";
}