# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.3.2 backend service for image and video generation using Alibaba Cloud Bailian API. The application uses:

- **JDK 17** - Java runtime
- **Spring Boot 3.3.2** - Main framework
- **MyBatis Plus 3.5.3.1** - Database ORM
- **MySQL 8.0** - Database
- **Lombok** - Code generation
- **WebFlux** - HTTP client for API calls
- **Alibaba Cloud Bailian** - Text-to-image and text-to-video generation

## Common Commands

### Build and Run
```bash
# Build the project
./mvnw clean compile

# Run tests
./mvnw test

# Package the application
./mvnw clean package

# Run the application
./mvnw spring-boot:run

# Run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Database
```bash
# Create database
mysql -u root -p -e "CREATE DATABASE img_video_generate CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# Create tables (run this SQL)
CREATE TABLE t_generation_task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_type VARCHAR(10) NOT NULL COMMENT 'IMAGE or VIDEO',
    prompt TEXT NOT NULL COMMENT 'User input prompt',
    status VARCHAR(20) NOT NULL COMMENT 'PENDING, PROCESSING, COMPLETED, FAILED',
    result_url TEXT COMMENT 'Generated result URL',
    alibaba_task_id VARCHAR(100) COMMENT 'Alibaba Cloud task ID',
    error_message TEXT COMMENT 'Error message if failed',
    parameters JSON COMMENT 'Request parameters in JSON',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    complete_time DATETIME NULL,
    INDEX idx_status (status),
    INDEX idx_task_type (task_type),
    INDEX idx_alibaba_task_id (alibaba_task_id)
);
```

## Architecture

### Package Structure
- `com.example.hello.entity` - JPA entities with MyBatis Plus annotations
- `com.example.hello.mapper` - MyBatis Plus mappers
- `com.example.hello.service` - Business logic services
- `com.example.hello.controller` - REST controllers
- `com.example.hello.dto` - Data Transfer Objects
- `com.example.hello.config` - Configuration classes
- `com.example.hello.exception` - Exception handling

### Key Components

#### Database Layer
- `GenerationTask` - Main entity for tracking generation requests
- `GenerationTaskMapper` - MyBatis Plus mapper with CRUD operations
- `MyBatisPlusConfig` - MyBatis Plus configuration with pagination

#### Service Layer
- `TextToImageService` - Handles text-to-image generation via Alibaba Cloud
- `TextToVideoService` - Handles text-to-video generation via Alibaba Cloud
- Both services manage task lifecycle and status tracking

#### API Layer
- `ImageGenerationController` - REST endpoints for image generation
- `VideoGenerationController` - REST endpoints for video generation
- `GlobalExceptionHandler` - Centralized error handling

### Configuration
- `application.properties` - Main configuration file
- `AlibabaCloudConfig` - Configuration properties for Alibaba Cloud API
- Database connection and MyBatis Plus settings

## API Endpoints

### Image Generation
- `POST /api/v1/image/generate` - Generate image from text
- `GET /api/v1/image/status/{taskId}` - Check image generation status

### Video Generation  
- `POST /api/v1/video/generate` - Generate video from text
- `GET /api/v1/video/status/{taskId}` - Check video generation status

## Configuration Requirements

### Environment Variables
Set these in `application.properties`:
- `alibaba.cloud.api-key` - Your Alibaba Cloud API key
- `spring.datasource.url` - MySQL database URL
- `spring.datasource.username` - Database username
- `spring.datasource.password` - Database password

### Alibaba Cloud Models
- Text-to-image: `wanx-v1`
- Text-to-video: `cogvideox-fun`

## Development Notes

- All database operations use MyBatis Plus for automatic CRUD
- API calls to Alibaba Cloud use WebClient (reactive)
- Task status is tracked asynchronously
- Comprehensive error handling via global exception handler
- Input validation using Bean Validation annotations
- Lombok reduces boilerplate code significantly

## Dependencies

Key dependencies are managed in `pom.xml`. The project uses Spring Boot parent for dependency management. Custom versions are specified for MySQL connector and MyBatis Plus.