package org.demo.asyncbatch.controller;

import org.demo.asyncbatch.model.BatchJobTracking;
import org.demo.asyncbatch.service.BatchJobTrackingService;
import org.demo.asyncbatch.service.TrackedAsyncBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/batch")
public class TrackedBatchController {

    @Autowired
    private TrackedAsyncBatchService trackedAsyncBatchService;

    @Autowired
    private BatchJobTrackingService trackingService;

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startBatchJob() {
        String jobId = UUID.randomUUID().toString();

        // Start batch processing asynchronously
        trackedAsyncBatchService.runBatchJobAsync(jobId);

        Map<String, Object> response = new HashMap<>();
        response.put("jobId", jobId);
        response.put("status", "PENDING");
        response.put("message", "Batch processing started");
        response.put("createdAt", LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{jobId}")
    public ResponseEntity<BatchJobTracking> getJobStatus(@PathVariable String jobId) {
        Optional<BatchJobTracking> tracking = trackedAsyncBatchService.getJobTracking(jobId);
        return tracking.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<BatchJobTracking>> getAllJobs() {
        List<BatchJobTracking> jobs = trackingService.findAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/jobs/active")
    public ResponseEntity<List<BatchJobTracking>> getActiveJobs() {
        List<BatchJobTracking> activeJobs = trackingService.findActiveJobs();
        return ResponseEntity.ok(activeJobs);
    }

    @GetMapping("/jobs/recent")
    public ResponseEntity<List<BatchJobTracking>> getRecentJobs() {
        List<BatchJobTracking> recentJobs = trackingService.findRecentJobs();
        return ResponseEntity.ok(recentJobs);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("statusCounts", trackingService.getJobStatusCounts());
        dashboard.put("activeJobs", trackingService.findActiveJobs());
        dashboard.put("recentJobs", trackingService.findRecentJobs());

        return ResponseEntity.ok(dashboard);
    }
}