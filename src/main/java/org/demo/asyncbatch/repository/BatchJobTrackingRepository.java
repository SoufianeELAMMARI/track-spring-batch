package org.demo.asyncbatch.repository;

import org.demo.asyncbatch.model.BatchJobTracking;
import org.demo.asyncbatch.model.JobStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BatchJobTrackingRepository extends JpaRepository<BatchJobTracking, String> {

    List<BatchJobTracking> findByStatus(JobStatusEnum status);
    List<BatchJobTracking> findTop10ByOrderByCreatedAtDesc();
    List<BatchJobTracking> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT j FROM BatchJobTracking j WHERE j.status IN ('PENDING', 'RUNNING')")
    List<BatchJobTracking> findActiveJobs();

    long countByStatus(JobStatusEnum status);
}