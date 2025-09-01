package org.demo.asyncbatch.config;

import org.demo.asyncbatch.model.JobStatusEnum;
import org.demo.asyncbatch.service.BatchJobTrackingService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleJobExecutionListener implements JobExecutionListener {

    @Autowired
    private BatchJobTrackingService trackingService;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        String jobId = jobExecution.getJobParameters().getString("jobId");
        if (jobId != null) {
            System.out.println("Job " + jobId + " is starting...");
            trackingService.updateJobStatus(jobId, JobStatusEnum.RUNNING);
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobId = jobExecution.getJobParameters().getString("jobId");
        if (jobId != null) {
            if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                trackingService.updateJobStatus(jobId, JobStatusEnum.COMPLETED);
                System.out.println("Job " + jobId + " completed successfully");
            } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
                String errorMessage = getErrorMessage(jobExecution);
                trackingService.setJobError(jobId, errorMessage);
                System.out.println("Job " + jobId + " failed: " + errorMessage);
            }
        }
    }

    private String getErrorMessage(JobExecution jobExecution) {
        StringBuilder errorMsg = new StringBuilder();
        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            for (Throwable throwable : stepExecution.getFailureExceptions()) {
                errorMsg.append(throwable.getMessage()).append("; ");
            }
        }
        return errorMsg.length() > 0 ? errorMsg.toString() : "Unknown error";
    }
}