package com.yiava.entity;

import java.time.LocalDateTime;

/**
 * Entity representing a converted markdown file for a specific page
 */
public class MarkdownFile {
    private Long id;
    private Long documentId;
    private Integer pageNumber;
    private String filePath;
    private Long fileSize;
    private String filename;
    private LocalDateTime createdAt;

    public MarkdownFile() {
    }

    public MarkdownFile(Long documentId, Integer pageNumber, String filePath,
                       String filename) {
        this.documentId = documentId;
        this.pageNumber = pageNumber;
        this.filePath = filePath;
        this.filename = filename;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "MarkdownFile{" +
                "id=" + id +
                ", documentId=" + documentId +
                ", pageNumber=" + pageNumber +
                ", filename='" + filename + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }
}
