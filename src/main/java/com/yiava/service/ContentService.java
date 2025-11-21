package com.yiava.service;

import com.yiava.entity.Content;
import com.yiava.mapper.ContentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Content business logic
 * Handles CRUD operations and validation for content records
 */
@Service
@Transactional
public class ContentService {

    private static final Logger logger = LoggerFactory.getLogger(ContentService.class);

    private final ContentMapper contentMapper;

    public ContentService(ContentMapper contentMapper) {
        this.contentMapper = contentMapper;
    }

    /**
     * Create a new content record
     *
     * @param contentText the text content to store
     * @return the created content entity with generated ID
     * @throws IllegalArgumentException if content is invalid
     */
    public Content create(String contentText) {
        logger.info("Creating new content record");

        // Validate input
        if (!StringUtils.hasText(contentText)) {
            logger.warn("Attempt to create content with empty text");
            throw new IllegalArgumentException("Content cannot be empty");
        }

        if (contentText.length() > 5000) {
            logger.warn("Attempt to create content exceeding length limit: {} characters", Integer.valueOf(contentText.length()));
            throw new IllegalArgumentException("Content must not exceed 5000 characters");
        }

        // Create and save content
        Content content = new Content(contentText);
        int rowsAffected = contentMapper.insert(content);

        if (rowsAffected == 0) {
            logger.error("Failed to insert content into database");
            throw new RuntimeException("Failed to create content");
        }

        logger.info("Successfully created content with ID: {}", content.getId());
        return content;
    }

    /**
     * Find content by ID
     *
     * @param id the content ID
     * @return Optional containing the content if found
     */
    @Transactional(readOnly = true)
    public Optional<Content> findById(Long id) {
        if (id == null || id <= 0) {
            logger.warn("Invalid ID requested: {}", id);
            return Optional.empty();
        }

        logger.debug("Finding content by ID: {}", id);
        Content content = contentMapper.findById(id);
        return Optional.ofNullable(content);
    }

    /**
     * Find all content records
     *
     * @return list of all content records
     */
    @Transactional(readOnly = true)
    public List<Content> findAll() {
        logger.debug("Finding all content records");
        return contentMapper.findAll();
    }

    /**
     * Update content by ID
     *
     * @param id the content ID
     * @param newContentText the updated content text
     * @return the updated content entity
     * @throws IllegalArgumentException if content is invalid
     * @throws RuntimeException if content not found or update failed
     */
    public Content update(Long id, String newContentText) {
        logger.info("Updating content with ID: {}", id);

        // Validate ID
        if (id == null || id <= 0) {
            logger.warn("Invalid ID for update: {}", id);
            throw new IllegalArgumentException("Invalid content ID");
        }

        // Check if content exists
        Optional<Content> existingContent = findById(id);
        if (existingContent.isEmpty()) {
            logger.warn("Content not found for ID: {}", id);
            throw new RuntimeException("Content not found with ID: " + id);
        }

        // Validate new content
        if (!StringUtils.hasText(newContentText)) {
            logger.warn("Attempt to update content with empty text");
            throw new IllegalArgumentException("Content cannot be empty");
        }

        if (newContentText.length() > 5000) {
            logger.warn("Attempt to update content exceeding length limit: {} characters", Integer.valueOf(newContentText.length()));
            throw new IllegalArgumentException("Content must not exceed 5000 characters");
        }

        // Update content
        int rowsAffected = contentMapper.updateById(id, newContentText);

        if (rowsAffected == 0) {
            logger.error("Failed to update content in database for ID: {}", id);
            throw new RuntimeException("Failed to update content");
        }

        // Retrieve updated content
        Content updatedContent = contentMapper.findById(id);
        logger.info("Successfully updated content with ID: {}", id);
        return updatedContent;
    }

    /**
     * Delete content by ID
     *
     * @param id the content ID
     * @throws RuntimeException if content not found or delete failed
     */
    public void delete(Long id) {
        logger.info("Deleting content with ID: {}", id);

        // Validate ID
        if (id == null || id <= 0) {
            logger.warn("Invalid ID for delete: {}", id);
            throw new IllegalArgumentException("Invalid content ID");
        }

        // Check if content exists
        Optional<Content> existingContent = findById(id);
        if (existingContent.isEmpty()) {
            logger.warn("Content not found for ID: {}", id);
            throw new RuntimeException("Content not found with ID: " + id);
        }

        // Delete content
        int rowsAffected = contentMapper.deleteById(id);

        if (rowsAffected == 0) {
            logger.error("Failed to delete content from database for ID: {}", id);
            throw new RuntimeException("Failed to delete content");
        }

        logger.info("Successfully deleted content with ID: {}", id);
    }

    /**
     * Count total content records
     *
     * @return the total count
     */
    @Transactional(readOnly = true)
    public int count() {
        logger.debug("Counting total content records");
        return contentMapper.count();
    }

    /**
     * Check if content exists by ID
     *
     * @param id the content ID
     * @return true if content exists
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return findById(id).isPresent();
    }
}
