# API Round - Spring Boot Webhook Application

A Spring Boot application that automatically generates webhooks, solves SQL problems, and submits solutions using JWT authentication.

## Quick Start

```bash
mvn spring-boot:run
```

The application automatically executes the full workflow on startup.

## Features

- **Automatic Execution**: Runs webhook generation and solution submission on startup
- **SQL Problem Solver**: Finds highest salary not paid on 1st day of month with employee details
- **JWT Authentication**: Uses bearer tokens for secure API communication
- **Testing Endpoints**: Multiple endpoints for debugging and manual testing

## API Workflow

1. **Generate Webhook**: POST to BFHL API with user credentials
2. **Solve SQL Problem**: Query for highest salary excluding 1st-day payments
3. **Submit Solution**: POST SQL query to webhook URL with JWT token

## Testing Endpoints

### Core Testing
- `GET /test/health` - Health check
- `GET /test/webhook` - Test webhook generation
- `GET /test/sql` - View SQL query
- `GET /test/full-flow` - Execute complete workflow
- `POST /test/submit` - Test solution submission

### Network Debugging
- `GET /network/test-dns` - Check DNS resolution
- `GET /network/test-connectivity` - Test internet connectivity
- `POST /network/force-webhook` - Manual webhook generation
- `GET /network/system-info` - System and network status

## SQL Solution

Finds highest salary not paid on 1st day of any month:

```sql
SELECT p.AMOUNT as SALARY, 
       CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) as NAME, 
       TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) as AGE, 
       d.DEPARTMENT_NAME 
FROM PAYMENTS p 
JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID 
JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID 
WHERE DAY(p.PAYMENT_TIME) != 1 
ORDER BY p.AMOUNT DESC 
LIMIT 1
```

## Configuration

**User Details** (hardcoded):
- Name: Pranav Yadav
- Reg No: 0101CS221095
- Email: pranav.2001y@gmail.com

## Troubleshooting

**DNS Resolution Failed**: 
- Check internet connection
- Try `/network/test-dns` endpoint
- Verify firewall settings

**401 Unauthorized**:
- Token format issue
- Use `/test/webhook` to verify token

**Connection Timeout**:
- Use `/network/test-connectivity`
- Try `/network/force-webhook` with extended timeout

## Project Structure

```
src/main/java/com/BAJAJ/API_ROUND/
├── ApiRoundApplication.java    # Main application
├── StartupService.java         # Auto-execution service
├── TestService.java           # Testing utilities
├── TestController.java        # Core test endpoints
└── NetworkTestController.java # Network debugging
```

## Dependencies

- Spring Boot Web
- Spring WebFlux
- Java 17+
