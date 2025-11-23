-- Flyway migration script
-- Version: 1.0
-- Description: Create content table for CRUD operations
-- Date: 2025-11-21

-- Create content table
CREATE TABLE IF NOT EXISTS content (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(5000) NOT NULL COMMENT 'Content text',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Content records table';

-- Insert sample data for testing (only for development)
-- This will only run in dev/test profiles when explicitly enabled
