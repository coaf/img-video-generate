package com.example.hello.dto;

import lombok.Data;

/**
 * Generation response DTO
 */
@Data
public class GenerationResponse {
    
    /**
     * Task ID for tracking
     */
    private Long taskId;
    
    /**
     * Task status
     */
    private String status;
    
    /**
     * Generated result URL
     */
    private String resultUrl;
    
    /**
     * Error message if generation failed
     */
    private String errorMessage;
    
    /**
     * Alibaba Cloud task ID
     */
    private String alibabaTaskId;
    
    public static GenerationResponse success(Long taskId, String status, String resultUrl) {
        GenerationResponse response = new GenerationResponse();
        response.setTaskId(taskId);
        response.setStatus(status);
        response.setResultUrl(resultUrl);
        return response;
    }
    
    public static GenerationResponse error(Long taskId, String errorMessage) {
        GenerationResponse response = new GenerationResponse();
        response.setTaskId(taskId);
        response.setStatus("FAILED");
        response.setErrorMessage(errorMessage);
        return response;
    }
}