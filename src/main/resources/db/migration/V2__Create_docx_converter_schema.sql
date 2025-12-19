-- Flyway migration script for Document to Markdown Conversion Service
-- Version: 2.0
-- Description: Create schema for document conversion features
-- Date: 2025-11-23

-- Create document table for storing uploaded document metadata
CREATE TABLE IF NOT EXISTS document (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_filename VARCHAR(255) NOT NULL COMMENT 'Original filename of uploaded document',
    file_extension VARCHAR(10) NOT NULL COMMENT 'File extension (doc, docx)',
    file_size BIGINT NOT NULL COMMENT 'File size in bytes',
    upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Upload timestamp',
    page_count INT DEFAULT 0 COMMENT 'Number of pages in the document',
    storage_path VARCHAR(500) NOT NULL COMMENT 'Storage location path',
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING' COMMENT 'Document processing status (PENDING, PROCESSING, COMPLETED, FAILED)',
    error_message TEXT COMMENT 'Error message if processing failed',
    INDEX idx_upload_time (upload_time),
    INDEX idx_status (status),
    INDEX idx_original_filename (original_filename)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Document metadata and processing status';

-- Create conversion_job table for tracking conversion progress
CREATE TABLE IF NOT EXISTS conversion_job (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    document_id BIGINT NOT NULL COMMENT 'Reference to document table',
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING' COMMENT 'Job status (PENDING, IN_PROGRESS, COMPLETED, FAILED)',
    progress_percentage INT DEFAULT 0 COMMENT 'Progress percentage (0-100)',
    current_page INT DEFAULT 0 COMMENT 'Current page being processed',
    total_pages INT DEFAULT 0 COMMENT 'Total pages to process',
    started_at TIMESTAMP NULL COMMENT 'Conversion start time',
    completed_at TIMESTAMP NULL COMMENT 'Conversion completion time',
    error_message TEXT COMMENT 'Error message if conversion failed',
    FOREIGN KEY (document_id) REFERENCES document(id) ON DELETE CASCADE,
    INDEX idx_document_id (document_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Conversion job tracking and progress';

-- Create markdown_file table for storing converted markdown files
CREATE TABLE IF NOT EXISTS markdown_file (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    document_id BIGINT NOT NULL COMMENT 'Reference to document table',
    page_number INT NOT NULL COMMENT 'Page number in original document',
    file_path VARCHAR(500) NOT NULL COMMENT 'Path to markdown file in storage',
    file_size BIGINT NOT NULL COMMENT 'File size in bytes',
    filename VARCHAR(255) NOT NULL COMMENT 'Generated markdown filename',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'File creation timestamp',
    FOREIGN KEY (document_id) REFERENCES document(id) ON DELETE CASCADE,
    UNIQUE KEY uk_document_page (document_id, page_number),
    INDEX idx_document_id (document_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Converted markdown files metadata';
