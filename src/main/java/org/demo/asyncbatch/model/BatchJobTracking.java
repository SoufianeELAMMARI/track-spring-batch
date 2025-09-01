package org.demo.asyncbatch.model;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "batch_job_tracking")
public class BatchJobTracking {

    @Id
    private String jobId;

    @Column(name = "job_name", nullable = false)
    private String jobName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private JobStatusEnum status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "total_items")
    private Integer totalItems;

    @Column(name = "processed_items")
    private Integer processedItems;

    @Column(name = "failed_items")
    private Integer failedItems;

    @Column(name = "progress_percentage")
    private Double progressPercentage;

    @Column(name = "job_parameters", columnDefinition = "TEXT")
    private String jobParameters;

    // Default constructor
    public BatchJobTracking() {}

    // Constructor for new job
    public BatchJobTracking(String jobId, String jobName) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.status = JobStatusEnum.PENDING;
        this.createdAt = LocalDateTime.now();
        this.processedItems = 0;
        this.failedItems = 0;
        this.progressPercentage = 0.0;
    }

    // Getters and Setters
    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }

    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }

    public JobStatusEnum getStatus() { return status; }
    public void setStatus(JobStatusEnum status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public Integer getTotalItems() { return totalItems; }
    public void setTotalItems(Integer totalItems) { this.totalItems = totalItems; }

    public Integer getProcessedItems() { return processedItems; }
    public void setProcessedItems(Integer processedItems) { this.processedItems = processedItems; }

    public Integer getFailedItems() { return failedItems; }
    public void setFailedItems(Integer failedItems) { this.failedItems = failedItems; }

    public Double getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Double progressPercentage) { this.progressPercentage = progressPercentage; }

    public String getJobParameters() { return jobParameters; }
    public void setJobParameters(String jobParameters) { this.jobParameters = jobParameters; }

    // Helper method to calculate duration
    public Long getDurationInSeconds() {
        if (startedAt == null) return null;
        LocalDateTime endTime = completedAt != null ? completedAt : LocalDateTime.now();
        return Duration.between(startedAt, endTime).getSeconds();
    }
}