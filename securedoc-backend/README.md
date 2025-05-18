# SecureDoc - Backend

## Project Overview
A file uploading and sharing application allowing authenticated users to upload files and share them via secure links.

## Backend Architecture

The Spring Boot backend will provide a RESTful API service with the following components:

- **Spring Boot REST API**: Core API application
- **PostgreSQL Database**: Data persistence layer
- **AWS S3**: File storage system
- **Spring Security + JWT**: Authentication and authorization

## Backend Implementation Plan

### 1. Project Setup
- Create a Spring Boot project with:
    - Spring Web
    - Spring Security
    - Spring Data JPA
    - PostgreSQL Driver
    - AWS SDK
    - Lombok
    - Validation
    - DevTools

### 2. Database Design
Create entity classes for:
- User
- Collection
- Image
- (Additional security entities)

Set up JPA repositories and define entity relationships.

### 3. Authentication System
- Implement JWT token-based authentication
- Create endpoints for registration and login
- Set up user management functionality

### 4. File Management
- Build file upload controller and service
- Implement S3 integration for file storage
- Create endpoints for:
    - File upload
    - File listing (by user or collection)
    - Collection management
    - Image sharing via public links

### 5. Security Implementation
- Configure CORS for frontend access
- Implement authentication filters
- Secure endpoints with proper authorization

### 6. API Design
- RESTful endpoints for all functionality
- Proper error handling and response status codes
- Request validation
- Documented API with Swagger/OpenAPI

### 7. Cloud Services Integration
- Configure AWS S3 bucket access
- Implement secure file operations
- Set up appropriate permissions and policies

### 8. Testing and Deployment
- Unit and integration tests
- Configuration for different environments
- CI/CD integration

## API Endpoints Overview

1. **Authentication**
    - `POST /api/v1/auth/register`
    - `POST /api/v1/auth/login`
    - `POST /api/v1/auth/refresh`

2. **Files**
    - `POST /api/v1/files/upload`
    - `GET /api/v1/files`
    - `GET /api/v1/files/{id}`
    - `DELETE /api/v1/files/{id}`

3. **Collections**
    - `POST /api/v1/collections`
    - `GET /api/v1/collections`
    - `GET /api/v1/collections/{id}`
    - `DELETE /api/v1/collections/{id}`
    - `GET /api/v1/collections/{id}/files`

4. **Public Access**
    - `GET /api/v1/public/files/{shareId}`