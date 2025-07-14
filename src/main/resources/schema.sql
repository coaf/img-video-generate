-- Create generation_tasks table for H2 database
CREATE TABLE IF NOT EXISTS generation_tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_type VARCHAR(50) NOT NULL,
    prompt TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    result_url VARCHAR(500),
    alibaba_task_id VARCHAR(100),
    error_message TEXT,
    parameters TEXT,
    user_id VARCHAR(100),
    task_id VARCHAR(100) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    complete_time TIMESTAMP
);

-- Create index for better performance
CREATE INDEX IF NOT EXISTS idx_generation_tasks_user_id ON generation_tasks(user_id);
CREATE INDEX IF NOT EXISTS idx_generation_tasks_task_id ON generation_tasks(task_id);
CREATE INDEX IF NOT EXISTS idx_generation_tasks_status ON generation_tasks(status);