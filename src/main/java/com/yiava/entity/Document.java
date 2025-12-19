package com.yiava.entity;

import java.time.LocalDateTime;

/**
 * Entity representing a document uploaded for conversion
 */
public class Document {
    private Long id;
    private String originalFilename;
    private String fileExtension;
    private Long fileSize;
    private LocalDateTime uploadTime;
    private Integer pageCount;
    private String storagePath;
    private DocumentStatus status;
    private String errorMessage;

    public enum DocumentStatus {
        PENDING, PROCESSING, COMPLETED, FAILED
    }

    public Document() {
    }

    public Document(String originalFilename, String fileExtension, Long fileSize,
                    String storagePath) {
        this.originalFilename = originalFilename;
        this.fileExtension = fileExtension;
        this.fileSize = fileSize;
        this.storagePath = storagePath;
        this.status = DocumentStatus.PENDING;
        this.uploadTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", originalFilename='" + originalFilename + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                ", fileSize=" + fileSize +
                ", uploadTime=" + uploadTime +
                ", pageCount=" + pageCount +
                ", status=" + status +
                '}';
    }
}
