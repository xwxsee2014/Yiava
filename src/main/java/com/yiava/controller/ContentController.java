package com.yiava.controller;

import com.yiava.dto.ContentRequest;
import com.yiava.dto.ContentResponse;
import com.yiava.entity.Content;
import com.yiava.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Content CRUD operations
 * Handles HTTP requests and responses for content management
 */
@RestController
@RequestMapping("/content")
@Tag(name = "Content", description = "Content management operations")
public class ContentController {

    private static final Logger logger = LoggerFactory.getLogger(ContentController.class);

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    /**
     * Create a new content record
     *
     * @param request the content request containing text content
     * @return ResponseEntity with created ContentResponse and HTTP 201 status
     */
    @PostMapping
    @Operation(summary = "Create a new content record", description = "Create a new content record with the provided content text")
    public ResponseEntity<ContentResponse> createContent(@Valid @RequestBody ContentRequest request) {
        logger.info("Received request to create content");

        // Create content via service
        Content content = contentService.create(request.getContent());

        // Convert to response DTO
        ContentResponse response = toResponse(content);

        logger.info("Successfully created content with ID: {}", content.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get all content records
     *
     * @return ResponseEntity with list of ContentResponse and HTTP 200 status
     */
    @GetMapping
    @Operation(summary = "Get all content records", description = "Retrieve a list of all content records")
    public ResponseEntity<List<ContentResponse>> getAllContent() {
        logger.debug("Received request to get all content");

        List<Content> contentList = contentService.findAll();
        List<ContentResponse> responseList = contentList.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        logger.info("Returning {} content records", responseList.size());
        return ResponseEntity.ok(responseList);
    }

    /**
     * Get content by ID
     *
     * @param id the content ID
     * @return ResponseEntity with ContentResponse and HTTP 200 status
     * @throws RuntimeException if content not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get content record by ID", description = "Retrieve a specific content record by its unique identifier")
    public ResponseEntity<ContentResponse> getContentById(@PathVariable Long id) {
        logger.debug("Received request to get content by ID: {}", id);

        Content content = contentService.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Content not found with ID: {}", id);
                    return new RuntimeException("Content not found with ID: " + id);
                });

        ContentResponse response = toResponse(content);
        logger.info("Successfully retrieved content with ID: {}", id);
        return ResponseEntity.ok(response);
    }

    /**
     * Update content by ID
     *
     * @param id the content ID
     * @param request the content request containing updated text content
     * @return ResponseEntity with updated ContentResponse and HTTP 200 status
     * @throws RuntimeException if content not found
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update content record", description = "Update an existing content record with new content text")
    public ResponseEntity<ContentResponse> updateContent(
            @PathVariable Long id,
            @Valid @RequestBody ContentRequest request) {

        logger.info("Received request to update content with ID: {}", id);

        Content updatedContent = contentService.update(id, request.getContent());
        ContentResponse response = toResponse(updatedContent);

        logger.info("Successfully updated content with ID: {}", id);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete content by ID
     *
     * @param id the content ID
     * @return ResponseEntity with no content and HTTP 204 status
     * @throws RuntimeException if content not found
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete content record", description = "Delete a content record by its unique identifier")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        logger.info("Received request to delete content with ID: {}", id);

        contentService.delete(id);

        logger.info("Successfully deleted content with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Convert Content entity to ContentResponse DTO
     *
     * @param content the Content entity
     * @return the ContentResponse DTO
     */
    private ContentResponse toResponse(Content content) {
        return new ContentResponse(
                content.getId(),
                content.getContent(),
                content.getCreatedAt(),
                content.getUpdatedAt()
        );
    }
}
