package com.example.ivi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.ivi.entity.GenerationTask;

import java.util.List;

/**
 * Generation task mapper interface
 */
@Mapper
public interface GenerationTaskMapper {
    
    int insert(GenerationTask task);
    
    GenerationTask selectById(Long id);
    
    GenerationTask selectByTaskId(String taskId);
    
    int updateStatus(@Param("id") Long id, @Param("status") String status, @Param("updatedAt") java.time.LocalDateTime updatedAt);
    
    int updateStatusAndAlibabaTaskId(@Param("id") Long id, @Param("status") String status, @Param("alibabaTaskId") String alibabaTaskId, @Param("updatedAt") java.time.LocalDateTime updatedAt);
    
    int updateTaskCompletion(@Param("id") Long id, @Param("status") String status, @Param("resultUrl") String resultUrl, @Param("errorMessage") String errorMessage, @Param("completeTime") java.time.LocalDateTime completeTime, @Param("updatedAt") java.time.LocalDateTime updatedAt);
    
    List<GenerationTask> selectByUserId(String userId);
}