package com.yiava.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for Content create/update requests
 * Used for API requests to create or update content records
 */
public class ContentRequest {

    /**
     * The content text to be stored
     */
    @NotBlank(message = "Content cannot be blank")
    @Size(max = 5000, message = "Content must not exceed 5000 characters")
    private String content;

    /**
     * Default constructor
     */
    public ContentRequest() {
    }

    /**
     * Constructor with content
     *
     * @param content the content text
     */
    public ContentRequest(String content) {
        this.content = content;
    }

    /**
     * Get the content text
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the content text
     *
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

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
     * Override toString for better debugging
     */
    @Override
    public String toString() {
        return "ContentRequest{" +
                "content='" + (content != null ? content.substring(0, Math.min(50, content.length())) + "..." : null) + '\'' +
                '}';
    }
}
