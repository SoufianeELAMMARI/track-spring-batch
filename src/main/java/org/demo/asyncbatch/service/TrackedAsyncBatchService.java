package org.demo.asyncbatch.service;

import org.demo.asyncbatch.model.BatchJobTracking;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class TrackedAsyncBatchService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("trackedProcessDataJob")
    private Job trackedProcessDataJob;

    @Autowired
    private BatchJobTrackingService trackingService;

    @Async
    public void runBatchJobAsync(String jobId) {
        try {
            // Create tracking record with PENDING status
            trackingService.createJobTracking(jobId, "processDataJob");

            System.out.println("Starting batch job with ID: " + jobId);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("jobId", jobId)
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            // Job execution - status will be updated by JobExecutionListener
            jobLauncher.run(trackedProcessDataJob, jobParameters);

        } catch (Exception e) {
            System.err.println("Error running batch job: " + e.getMessage());
            try {
                trackingService.setJobError(jobId, "Failed to start job: " + e.getMessage());
            } catch (Exception trackingError) {
                System.err.println("Error updating job tracking: " + trackingError.getMessage());
            }
        }
    }

    public Optional<BatchJobTracking> getJobTracking(String jobId) {
        return trackingService.findByJobId(jobId);
    }
}