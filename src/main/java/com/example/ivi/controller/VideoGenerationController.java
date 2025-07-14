package com.example.ivi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.ivi.dto.GenerationResponse;
import com.example.ivi.dto.TextToVideoRequest;
import com.example.ivi.service.TextToVideoService;

import jakarta.validation.Valid;

/**
 * Video generation REST controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
@Validated
public class VideoGenerationController {
    
    private final TextToVideoService textToVideoService;
    
    /**
     * Generate video from text prompt
     */
    @PostMapping("/generate")
    public ResponseEntity<GenerationResponse> generateVideo(@Valid @RequestBody TextToVideoRequest request) {
        log.info("Received video generation request: {}", request.getPrompt());
        
        try {
            GenerationResponse response = textToVideoService.generateVideo(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error generating video", e);
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
            GenerationResponse response = textToVideoService.checkTaskStatus(taskId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error checking task status", e);
            return ResponseEntity.internalServerError()
                    .body(GenerationResponse.error(taskId, "Internal server error: " + e.getMessage()));
        }
    }
}