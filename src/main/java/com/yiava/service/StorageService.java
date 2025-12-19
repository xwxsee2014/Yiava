package com.yiava.service;

import com.yiava.config.StorageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Service for managing document storage
 */
@Service
public class StorageService {

    private static final Logger logger = LoggerFactory.getLogger(StorageService.class);

    private final StorageConfig storageConfig;

    @Autowired
    public StorageService(StorageConfig storageConfig) {
        this.storageConfig = storageConfig;
        initializeStorage();
    }

    /**
     * Initialize the base storage directory structure
     */
    private void initializeStorage() {
        try {
            Path basePath = Paths.get(storageConfig.getBasePath());
            if (!Files.exists(basePath)) {
                Files.createDirectories(basePath);
                logger.info("Created storage directory: {}", basePath);
            }
        } catch (IOException e) {
            logger.error("Failed to initialize storage directory", e);
            throw new RuntimeException("Storage initialization failed", e);
        }
    }

    /**
     * Generate storage path for a document based on current date
     * Format: basePath/yyyy/mm/{documentId}/
     */
    public String generateDocumentPath(Long documentId) {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        return Paths.get(storageConfig.getBasePath(), currentDate, documentId.toString())
                   .toString();
    }

    /**
     * Generate filename for a markdown page
     * Format: {baseName}-page-{pageNumber}.md
     */
    public String generateMarkdownFilename(String baseName, int pageNumber) {
        return String.format("%s-page-%d.md", baseName, pageNumber);
    }

    /**
     * Get the base storage path
     */
    public String getBasePath() {
        return storageConfig.getBasePath();
    }

    /**
     * Get retention period in days
     */
    public int getRetentionDays() {
        return storageConfig.getRetentionDays();
    }

    /**
     * Check if storage directory exists and is writable
     */
    public boolean isStorageAccessible() {
        try {
            Path basePath = Paths.get(storageConfig.getBasePath());
            return Files.exists(basePath) && Files.isDirectory(basePath) &&
                   Files.isWritable(basePath);
        } catch (Exception e) {
            logger.error("Storage accessibility check failed", e);
            return false;
        }
    }
}
