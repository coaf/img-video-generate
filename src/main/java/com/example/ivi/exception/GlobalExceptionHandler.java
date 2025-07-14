package com.example.ivi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.ivi.dto.GenerationResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler for REST controllers
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handle validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenerationResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        
        String errorMessage = "Validation failed: " + String.join(", ", errors);
        log.warn("Validation error: {}", errorMessage);
        
        return ResponseEntity.badRequest()
                .body(GenerationResponse.error(null, errorMessage));
    }
    
    /**
     * Handle bind exceptions
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<GenerationResponse> handleBindException(BindException ex) {
        List<String> errors = new ArrayList<>();
        
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        
        String errorMessage = "Bind error: " + String.join(", ", errors);
        log.warn("Bind error: {}", errorMessage);
        
        return ResponseEntity.badRequest()
                .body(GenerationResponse.error(null, errorMessage));
    }
    
    /**
     * Handle illegal argument exceptions
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GenerationResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        
        return ResponseEntity.badRequest()
                .body(GenerationResponse.error(null, ex.getMessage()));
    }
    
    /**
     * Handle runtime exceptions
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GenerationResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime error", ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(GenerationResponse.error(null, "Internal server error: " + ex.getMessage()));
    }
    
    /**
     * Handle generic exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenerationResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(GenerationResponse.error(null, "Unexpected error occurred"));
    }
}