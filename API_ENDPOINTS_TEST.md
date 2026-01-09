# API Endpoints Testing Guide

Base URL: `http://localhost:8080` (default Spring Boot port)

---

## 1. Authentication Endpoints (`/api/v1/auth`)

### 1.1 Register User
**Method:** `POST`  
**Endpoint:** `/api/v1/auth/register`  
**Request Body:**
```json
{
  "email": "user@example.com",
  "name": "John Doe",
  "userName": "johndoe",
  "phoneNumber": "+1234567890",
  "role": "USER",
  "organizationId": "org-123",
  "status": "ACTIVE"
}
```

### 1.2 Login
**Method:** `POST`  
**Endpoint:** `/api/v1/auth/login`  
**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "yourpassword"
}
```

### 1.3 Logout
**Method:** `POST`  
**Endpoint:** `/api/v1/auth/logout`  
**Request Body:** None (empty body `{}`)

### 1.4 Get Current User
**Method:** `GET`  
**Endpoint:** `/api/v1/auth/me`  
**Request Body:** None

### 1.5 Get Session Info
**Method:** `GET`  
**Endpoint:** `/api/v1/auth/session-info`  
**Request Body:** None

---

## 2. User Endpoints (`/api/v1/users`)

### 2.1 Get User by ID
**Method:** `GET`  
**Endpoint:** `/api/v1/users/get/{id}`  
**Example:** `/api/v1/users/get/user-123`  
**Request Body:** None

### 2.2 Get User by Email
**Method:** `GET`  
**Endpoint:** `/api/v1/users/{email}`  
**Example:** `/api/v1/users/user@example.com`  
**Request Body:** None

### 2.3 Get Users by Organization ID
**Method:** `GET`  
**Endpoint:** `/api/v1/users/{organizationId}`  
**Example:** `/api/v1/users/org-123`  
**Request Body:** None

**Note:** There's a potential conflict between endpoints 2.2 and 2.3 as both use the same pattern. The first match will be used.

### 2.4 Find All Users
**Method:** `POST`  
**Endpoint:** `/api/v1/users/find-all`  
**Request Body:**
```json
{
  "search": "john",
  "getAll": false,
  "page": 0,
  "size": 10
}
```

**Example with all parameters:**
```json
{
  "search": "john",
  "getAll": false,
  "page": 0,
  "size": 10
}
```

**Example with pagination only:**
```json
{
  "page": 1,
  "size": 20
}
```

**Example to get all users:**
```json
{
  "getAll": true
}
```

**Minimal request (uses defaults):**
```json
{}
```

### 2.5 Update User
**Method:** `POST`  
**Endpoint:** `/api/v1/users/update`  
**Request Body:**
```json
{
  "id": "user-123",
  "name": "John Updated",
  "phoneNumber": "+9876543210",
  "email": "updated@example.com",
  "role": "ADMIN",
  "organizationId": "org-456",
  "status": "INACTIVE"
}
```

**Partial update example (only update name and phone):**
```json
{
  "id": "user-123",
  "name": "John Updated",
  "phoneNumber": "+9876543210"
}
```

---

## 3. Organization Endpoints (`/api/v1/organizations`)

### 3.1 Create Organization
**Method:** `POST`  
**Endpoint:** `/api/v1/organizations/create`  
**Request Body:**
```json
{
  "name": "Acme Corporation",
  "phoneNumber": "+1234567890",
  "email": "contact@acme.com",
  "status": "ACTIVE"
}
```

**Minimal request:**
```json
{
  "name": "Acme Corporation",
  "email": "contact@acme.com"
}
```

### 3.2 Get Organization by ID
**Method:** `GET`  
**Endpoint:** `/api/v1/organizations/get/{id}`  
**Example:** `/api/v1/organizations/get/org-123`  
**Request Body:** None

### 3.3 Get Organization by Email
**Method:** `GET`  
**Endpoint:** `/api/v1/organizations/email/{email}`  
**Example:** `/api/v1/organizations/email/contact@acme.com`  
**Request Body:** None

### 3.4 Find All Organizations
**Method:** `POST`  
**Endpoint:** `/api/v1/organizations/find-all`  
**Request Body:**
```json
{
  "search": "acme",
  "getAll": false,
  "page": 0,
  "size": 10
}
```

**Example with all parameters:**
```json
{
  "search": "acme",
  "getAll": false,
  "page": 0,
  "size": 10
}
```

**Example to get all organizations:**
```json
{
  "getAll": true
}
```

**Minimal request (uses defaults):**
```json
{}
```

### 3.5 Update Organization
**Method:** `POST`  
**Endpoint:** `/api/v1/organizations/update`  
**Request Body:**
```json
{
  "id": "org-123",
  "name": "Acme Corporation Updated",
  "phoneNumber": "+9876543210",
  "email": "newemail@acme.com",
  "status": "INACTIVE"
}
```

**Partial update example:**
```json
{
  "id": "org-123",
  "name": "Acme Corporation Updated",
  "phoneNumber": "+9876543210"
}
```

---

## Testing with cURL Examples

### Register
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "name": "John Doe",
    "userName": "johndoe",
    "phoneNumber": "+1234567890",
    "role": "USER",
    "organizationId": "org-123",
    "status": "ACTIVE"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "yourpassword"
  }'
```

### Find All Users
```bash
curl -X POST http://localhost:8080/api/v1/users/find-all \
  -H "Content-Type: application/json" \
  -d '{
    "search": "john",
    "getAll": false,
    "page": 0,
    "size": 10
  }'
```

### Update User
```bash
curl -X POST http://localhost:8080/api/v1/users/update \
  -H "Content-Type: application/json" \
  -d '{
    "id": "user-123",
    "name": "John Updated",
    "phoneNumber": "+9876543210"
  }'
```

### Create Organization
```bash
curl -X POST http://localhost:8080/api/v1/organizations/create \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Acme Corporation",
    "phoneNumber": "+1234567890",
    "email": "contact@acme.com",
    "status": "ACTIVE"
  }'
```

---

## Testing with Postman/Thunder Client

1. Import the requests using the examples above
2. Set base URL: `http://localhost:8080`
3. For endpoints requiring authentication, ensure you're logged in first to maintain session
4. All POST requests should have `Content-Type: application/json` header

---

## Notes

- All optional fields in request bodies can be omitted (null values)
- For GET requests, replace path variables (`{id}`, `{email}`, etc.) with actual values
- Session-based authentication: Login first to establish a session for protected endpoints
- Status values are typically "ACTIVE" or "INACTIVE"
- Role values might be "USER", "ADMIN", etc. (check your enum/constants)
