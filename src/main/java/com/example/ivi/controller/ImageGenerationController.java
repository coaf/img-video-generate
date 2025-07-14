package com.example.ivi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.ivi.dto.GenerationResponse;
import com.example.ivi.dto.TextToImageRequest;
import com.example.ivi.service.TextToImageService;

import jakarta.validation.Valid;

/**
 * Image generation REST controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
@Validated
public class ImageGenerationController {
    
    private final TextToImageService textToImageService;
    
    /**
     * Generate image from text prompt
     */
    @PostMapping("/generate")
    public ResponseEntity<GenerationResponse> generateImage(@Valid @RequestBody TextToImageRequest request) {
        log.info("Received image generation request: {}", request.getPrompt());
        
        try {
            GenerationResponse response = textToImageService.generateImage(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error generating image", e);
            return ResponseEntity.internalServerError()
                    .body(GenerationResponse.error(null, "Internal server error: " + e.getMessage()));
        }
    }
    
    /**
     * Check generation task status
     */
    @GetMapping("/status/{taskId}")
    public ResponseEntity<GenerationResponse> checkTaskStatus(@PathVariable Long taskId) {
        log.info("Checking status for task: {}", taskId);
        
        try {
            GenerationResponse response = textToImageService.checkTaskStatus(taskId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error checking task status", e);
            return ResponseEntity.internalServerError()
                    .body(GenerationResponse.error(taskId, "Internal server error: " + e.getMessage()));
        }
    }
}