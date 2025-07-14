package com.example.hello.mapper;

import com.example.hello.entity.GenerationTask;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * Generation task mapper interface
 */
@Mapper
public interface GenerationTaskMapper {
    
    /**
     * Insert a new generation task
     */
    @Insert("INSERT INTO t_generation_task (task_id, user_id, task_type, prompt, status, created_at, updated_at) " +
            "VALUES (#{taskId}, #{userId}, #{taskType}, #{prompt}, #{status}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(GenerationTask task);
    
    /**
     * Select task by ID
     */
    @Select("SELECT * FROM t_generation_task WHERE id = #{id}")
    GenerationTask selectById(Long id);
    
    /**
     * Select task by task ID
     */
    @Select("SELECT * FROM t_generation_task WHERE task_id = #{taskId}")
    GenerationTask selectByTaskId(String taskId);
    
    /**
     * Update task status
     */
    @Update("UPDATE t_generation_task SET status = #{status}, updated_at = #{updatedAt} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status, @Param("updatedAt") java.time.LocalDateTime updatedAt);
    
    /**
     * Select all tasks by user ID
     */
    @Select("SELECT * FROM t_generation_task WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<GenerationTask> selectByUserId(String userId);
}