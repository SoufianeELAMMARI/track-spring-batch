CREATE TABLE batch_job_tracking (
                                    job_id VARCHAR(36) PRIMARY KEY,
                                    job_name VARCHAR(100) NOT NULL,
                                    status VARCHAR(20) NOT NULL,
                                    created_at TIMESTAMP NOT NULL,
                                    started_at TIMESTAMP NULL,
                                    completed_at TIMESTAMP NULL,
                                    error_message TEXT NULL
);

CREATE INDEX idx_batch_job_status ON batch_job_tracking(status);
CREATE INDEX idx_batch_job_created_at ON batch_job_tracking(created_at);