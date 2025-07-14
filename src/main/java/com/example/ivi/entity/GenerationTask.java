package com.example.ivi.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Generation task entity for tracking image and video generation requests
 */
@Data
public class GenerationTask {
    
    private Long id;
    
    /**
     * Task type: IMAGE or VIDEO
     */
    private String taskType;
    
    /**
     * User input prompt for generation
     */
    private String prompt;
    
    /**
     * Task status: PENDING, PROCESSING, COMPLETED, FAILED
     */
    private String status;
    
    /**
     * Generated result URL or file path
     */
    private String resultUrl;
    
    /**
     * Alibaba Cloud task ID for tracking
     */
    private String alibabaTaskId;
    
    /**
     * Error message if generation failed
     */
    private String errorMessage;
    
    /**
     * Additional parameters in JSON format
     */
    private String parameters;
    
    /**
     * User ID who created the task
     */
    private String userId;
    
    /**
     * Unique task identifier
     */
    private String taskId;
    
    /**
     * Task creation time
     */
    private LocalDateTime createdAt;
    
    /**
     * Task last update time
     */
    private LocalDateTime updatedAt;
    
    /**
     * Task completion time
     */
    private LocalDateTime completeTime;
}