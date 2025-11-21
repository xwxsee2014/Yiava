package com.yiava.entity;

import java.time.LocalDateTime;

/**
 * Content entity representing a content record in the database
 * Maps to the 'content' table with auto-incrementing ID and VARCHAR content field
 */
public class Content {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Default constructor
     */
    public Content() {
    }

    /**
     * Constructor with content
     *
     * @param content the content text
     */
    public Content(String content) {
        this.content = content;
    }

    /**
     * Full constructor
     *
     * @param id the unique identifier
     * @param content the content text
     * @param createdAt creation timestamp
     * @param updatedAt last update timestamp
     */
    public Content(Long id, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Get the unique identifier
     *
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the unique identifier
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
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
     * Get the creation timestamp
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Set the creation timestamp
     *
     * @param createdAt the creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Get the last update timestamp
     *
     * @return the update timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Set the last update timestamp
     *
     * @param updatedAt the update timestamp to set
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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
     * Check if the content is valid (not empty and within length limit)
     *
     * @return true if content is valid
     */
    public boolean isValid() {
        return content != null && !content.trim().isEmpty() && content.length() <= 5000;
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
        return "Content{" +
                "id=" + id +
                ", content='" + (content != null ? content.substring(0, Math.min(50, content.length())) + "..." : null) + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    /**
     * Override equals for proper comparison
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content1 = (Content) o;
        return id != null && id.equals(content1.id);
    }

    /**
     * Override hashCode for proper hashing
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
