# Spring Batch Async Processing

A Spring Boot application that returns a UUID immediately and processes batch jobs in the background with database tracking.

## 🚀 Features

- **Immediate UUID Response**: Get job ID instantly while processing runs in background
- **Database Tracking**: Job status persistence (PENDING → RUNNING → COMPLETED/FAILED)
- **REST API**: Simple endpoints for job management

## 📋 Quick Setup

```bash
# Clone and run
git clone <repository-url>
cd spring-batch-async
mvn spring-boot:run
```

Application starts at `http://localhost:8080`

## 🌐 API Endpoints

### Start Job
```bash
POST /api/batch/start
```
**Response:**
```json
{
    "jobId": "uuid-here",
    "status": "PENDING",
    "message": "Batch processing started"
}
```

### Check Status
```bash
GET /api/batch/status/{jobId}
```
**Response:**
```json
{
    "jobId": "uuid-here",
    "status": "RUNNING",
    "createdAt": "2024-01-15T10:30:00",
    "startedAt": "2024-01-15T10:30:05",
    "durationInSeconds": 25
}
```

### View All Jobs
```bash
GET /api/batch/jobs
GET /api/batch/dashboard
```

## 🧪 Test Example

```bash
# 1. Start job
curl -X POST http://localhost:8080/api/batch/start

# 2. Check status (copy jobId from step 1)
curl http://localhost:8080/api/batch/status/YOUR_JOB_ID

# 3. View dashboard
curl http://localhost:8080/api/batch/dashboard
```

## 🔍 Database

- **H2 Console**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa` (no password)

## ⚙️ Configuration

```properties
# application.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
spring.batch.job.enabled=false
```

## 📊 Job Flow

```
PENDING → RUNNING → COMPLETED/FAILED
```

Processing time: ~10 seconds (5 items × 2 seconds each)
