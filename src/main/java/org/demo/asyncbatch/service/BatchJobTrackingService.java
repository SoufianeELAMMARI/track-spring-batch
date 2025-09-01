package org.demo.asyncbatch.service;

import org.demo.asyncbatch.model.BatchJobTracking;
import org.demo.asyncbatch.repository.BatchJobTrackingRepository;
import org.demo.asyncbatch.model.JobStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class BatchJobTrackingService {

    @Autowired
    private BatchJobTrackingRepository repository;

    public BatchJobTracking createJobTracking(String jobId, String jobName) {
        BatchJobTracking tracking = new BatchJobTracking(jobId, jobName);
        return repository.save(tracking);
    }

    public BatchJobTracking updateJobStatus(String jobId, JobStatusEnum status) {
        Optional<BatchJobTracking> optionalTracking = repository.findById(jobId);
        if (optionalTracking.isPresent()) {
            BatchJobTracking tracking = optionalTracking.get();
            tracking.setStatus(status);

            if (status == JobStatusEnum.RUNNING && tracking.getStartedAt() == null) {
                tracking.setStartedAt(LocalDateTime.now());
            } else if (status == JobStatusEnum.COMPLETED || status == JobStatusEnum.FAILED) {
                tracking.setCompletedAt(LocalDateTime.now());
            }

            return repository.save(tracking);
        }
        throw new RuntimeException("Job tracking not found for ID: " + jobId);
    }

    public BatchJobTracking setJobError(String jobId, String errorMessage) {
        Optional<BatchJobTracking> optionalTracking = repository.findById(jobId);
        if (optionalTracking.isPresent()) {
            BatchJobTracking tracking = optionalTracking.get();
            tracking.setStatus(JobStatusEnum.FAILED);
            tracking.setErrorMessage(errorMessage);
            tracking.setCompletedAt(LocalDateTime.now());

            return repository.save(tracking);
        }
        throw new RuntimeException("Job tracking not found for ID: " + jobId);
    }

    public Optional<BatchJobTracking> findByJobId(String jobId) {
        return repository.findById(jobId);
    }

    public List<BatchJobTracking> findAllJobs() {
        return repository.findAll();
    }

    public List<BatchJobTracking> findActiveJobs() {
        return repository.findActiveJobs();
    }

    public List<BatchJobTracking> findRecentJobs() {
        return repository.findTop10ByOrderByCreatedAtDesc();
    }

    public Map<JobStatusEnum, Long> getJobStatusCounts() {
        Map<JobStatusEnum, Long> counts = new HashMap<>();
        for (JobStatusEnum status : JobStatusEnum.values()) {
            counts.put(status, repository.countByStatus(status));
        }
        return counts;
    }
}