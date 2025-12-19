package com.yiava.entity;

import java.time.LocalDateTime;

/**
 * Entity representing a conversion job tracking document processing
 */
public class ConversionJob {
    private Long id;
    private Long documentId;
    private ConversionJobStatus status;
    private Integer progressPercentage;
    private Integer currentPage;
    private Integer totalPages;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String errorMessage;

    public enum ConversionJobStatus {
        PENDING, IN_PROGRESS, COMPLETED, FAILED
    }

    public ConversionJob() {
    }

    public ConversionJob(Long documentId) {
        this.documentId = documentId;
        this.status = ConversionJobStatus.PENDING;
        this.progressPercentage = 0;
        this.currentPage = 0;
        this.totalPages = 0;
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

    public ConversionJobStatus getStatus() {
        return status;
    }

    public void setStatus(ConversionJobStatus status) {
        this.status = status;
    }

    public Integer getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Integer progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ConversionJob{" +
                "id=" + id +
                ", documentId=" + documentId +
                ", status=" + status +
                ", progressPercentage=" + progressPercentage +
                ", currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                '}';
    }
}
