package com.yiava.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Content responses
 * Java 17 record for immutable data carrier
 * Used for API responses to return content data to clients
 *
 * @param id the unique identifier of the content record
 * @param content the text content
 * @param createdAt timestamp when the record was created
 * @param updatedAt timestamp when the record was last updated
 */
public record ContentResponse(
        Long id,
        String content,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) {

    /**
     * Check if the content is empty
     *
     * @return true if content is null or empty
     */
    public boolean isEmpty() {
        return content == null || content.trim().isEmpty();
    }

    /**
     * Get the content length
     *
     * @return the length of content text
     */
    public int getContentLength() {
        return content != null ? content.length() : 0;
    }

    /**
     * Check if the record was created today
     *
     * @return true if created today
     */
    public boolean isCreatedToday() {
        if (createdAt == null) {
            return false;
        }
        return createdAt.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }

    /**
     * Override toString for better debugging
     */
    @Override
    public String toString() {
        return "ContentResponse{" +
                "id=" + id +
                ", content='" + (content != null ? content.substring(0, Math.min(50, content.length())) + "..." : null) + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
