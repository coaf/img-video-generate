package com.example.ivi.service;

import com.example.ivi.config.AlibabaCloudConfig;
import com.example.ivi.dto.GenerationResponse;
import com.example.ivi.dto.TextToImageRequest;
import com.example.ivi.entity.GenerationTask;
import com.example.ivi.mapper.GenerationTaskMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Text to image generation service using Alibaba Cloud Bailian API
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TextToImageService {
    
    private final AlibabaCloudConfig alibabaCloudConfig;
    private final GenerationTaskMapper generationTaskMapper;
    private final ObjectMapper objectMapper;
    private final WebClient webClient = WebClient.builder().build();
    
    /**
     * Generate image from text prompt
     */
    public GenerationResponse generateImage(TextToImageRequest request) {
        log.info("Starting text to image generation with prompt: {}", request.getPrompt());
        
        // Create task record
        GenerationTask task = new GenerationTask();
        task.setTaskType("IMAGE");
        task.setPrompt(request.getPrompt());
        task.setStatus("PENDING");
        task.setUserId("default"); // TODO: Get from authentication context
        task.setTaskId(java.util.UUID.randomUUID().toString());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        
        try {
            // Save task parameters as JSON
            String parameters = objectMapper.writeValueAsString(request);
            task.setParameters(parameters);
            
            generationTaskMapper.insert(task);
            
            // Call Alibaba Cloud API
            String alibabaTaskId = callAlibabaImageAPI(request);
            
            // Update task with Alibaba task ID
            task.setAlibabaTaskId(alibabaTaskId);
            task.setStatus("PROCESSING");
            task.setUpdatedAt(LocalDateTime.now());
            generationTaskMapper.updateStatusAndAlibabaTaskId(task.getId(), task.getStatus(), alibabaTaskId, task.getUpdatedAt());
            
            log.info("Text to image generation started successfully, task ID: {}, Alibaba task ID: {}", 
                    task.getId(), alibabaTaskId);
            
            return GenerationResponse.success(task.getId(), "PROCESSING", null);
            
        } catch (Exception e) {
            log.error("Failed to generate image", e);
            task.setStatus("FAILED");
            task.setErrorMessage(e.getMessage());
            task.setUpdatedAt(LocalDateTime.now());
            generationTaskMapper.updateStatus(task.getId(), task.getStatus(), task.getUpdatedAt());
            
            return GenerationResponse.error(task.getId(), e.getMessage());
        }
    }
    
    /**
     * Call Alibaba Cloud text-to-image API
     */
    private String callAlibabaImageAPI(TextToImageRequest request) {
        try {
            // Prepare request body according to Alibaba Cloud Bailian API documentation
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", alibabaCloudConfig.getTextToImage().getModel());
            
            Map<String, Object> input = new HashMap<>();
            input.put("prompt", request.getPrompt());
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("size", request.getSize());
            parameters.put("n", request.getN());
            // Note: quality and style parameters may not be supported by all model versions
            // Uncomment if your model version supports these parameters:
            // parameters.put("quality", request.getQuality());
            // parameters.put("style", request.getStyle());
            
            requestBody.put("input", input);
            requestBody.put("parameters", parameters);
            
            // Make API call
            String response = webClient.post()
                    .uri(alibabaCloudConfig.getBaseUrl() + "/services/aigc/text2image/image-synthesis")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + alibabaCloudConfig.getApiKey())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header("X-DashScope-Async", "enable")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            // Parse response to get task ID
            JsonNode responseNode = objectMapper.readTree(response);
            
            if (responseNode.has("output") && responseNode.get("output").has("task_id")) {
                return responseNode.get("output").get("task_id").asText();
            } else {
                throw new RuntimeException("Failed to get task ID from Alibaba Cloud response: " + response);
            }
            
        } catch (Exception e) {
            log.error("Error calling Alibaba Cloud text-to-image API", e);
            throw new RuntimeException("Failed to call Alibaba Cloud API: " + e.getMessage(), e);
        }
    }
    
    /**
     * Check task status and get result
     */
    public GenerationResponse checkTaskStatus(Long taskId) {
        GenerationTask task = generationTaskMapper.selectById(taskId);
        if (task == null) {
            return GenerationResponse.error(taskId, "Task not found");
        }
        
        // If task is still processing, check status from Alibaba Cloud
        if ("PROCESSING".equals(task.getStatus())) {
            try {
                checkAlibabaTaskStatus(task);
                task.setUpdatedAt(LocalDateTime.now());
                generationTaskMapper.updateTaskCompletion(task.getId(), task.getStatus(), task.getResultUrl(), task.getErrorMessage(), task.getCompleteTime(), task.getUpdatedAt());
            } catch (Exception e) {
                log.error("Error checking task status from Alibaba Cloud", e);
                task.setStatus("FAILED");
                task.setErrorMessage(e.getMessage());
                task.setUpdatedAt(LocalDateTime.now());
                generationTaskMapper.updateTaskCompletion(task.getId(), task.getStatus(), null, task.getErrorMessage(), LocalDateTime.now(), task.getUpdatedAt());
            }
        }
        
        return GenerationResponse.success(task.getId(), task.getStatus(), task.getResultUrl());
    }
    
    /**
     * Check task status from Alibaba Cloud
     */
    private void checkAlibabaTaskStatus(GenerationTask task) {
        try {
            String response = webClient.get()
                    .uri(alibabaCloudConfig.getBaseUrl() + "/tasks/" + task.getAlibabaTaskId())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + alibabaCloudConfig.getApiKey())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            JsonNode responseNode = objectMapper.readTree(response);
            
            if (responseNode.has("output")) {
                JsonNode output = responseNode.get("output");
                String taskStatus = output.get("task_status").asText();
                
                if ("SUCCEEDED".equals(taskStatus)) {
                    task.setStatus("COMPLETED");
                    task.setCompleteTime(LocalDateTime.now());
                    
                    // Extract result URL
                    if (output.has("results") && output.get("results").isArray() && 
                        output.get("results").size() > 0) {
                        String resultUrl = output.get("results").get(0).get("url").asText();
                        task.setResultUrl(resultUrl);
                    }
                } else if ("FAILED".equals(taskStatus)) {
                    task.setStatus("FAILED");
                    task.setErrorMessage("Task failed on Alibaba Cloud");
                    task.setCompleteTime(LocalDateTime.now());
                }
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to check task status from Alibaba Cloud: " + e.getMessage(), e);
        }
    }
}