# 🛍️ MELI Order Management Service

Jared Alexander Trujillo Ortiz  
**Date:** October 16, 2025  

A robust and scalable microservice for managing e-commerce orders, built with **Java** and **Spring Boot** as a solution to critical issues faced by MELI.

---

## 🧩 1. Project Context: The MELI Challenge

This project was developed to address significant technical failures in MELI's original order management system. The previous system suffered from operational issues caused by environment misconfigurations and database instability, resulting in substantial business losses and customer complaints.

This new service provides a modern, reliable, and scalable replacement. It implements best practices such as:

- Environment-specific configurations (preventing the original error)
- Robust database interactions
- Comprehensive API documentation via Swagger
- Clean, maintainable architecture

Together, these establish a solid foundation for MELI's future e-commerce operations.

---

## 🚀 2. Key Features

- **Full CRUD Functionality:** Create, Read, Update (via save), and Delete operations for orders.  
- **Multi-Item Orders:** Models real-world carts with multiple products (line items).  
- **Soft Deletion:** Orders are marked as deleted using a timestamp (`deletedAt`) instead of being removed permanently.  
- **Environment Profiles:** Uses Spring Profiles (`dev`, `prod`) for distinct configurations.  
  - `dev`: H2 in-memory database.  
  - `prod`: PostgreSQL database via environment variables.  
- **Interactive API Documentation:** Swagger UI for clear, testable endpoints.  
- **Centralized Exception Handling:** Consistent JSON error responses (e.g., *Order Not Found*).

---

## ⚙️ 3. Tech Stack

| Component | Technology |
|------------|-------------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.2.5 |
| **Persistence** | Spring Data JPA (Hibernate) |
| **Build Tool** | Maven |
| **Databases** | H2 (Dev) / PostgreSQL (Prod) |
| **API Docs** | SpringDoc OpenAPI (Swagger UI) |

---

## 🧭 4. Getting Started

### ✅ Prerequisites

- Java JDK 17+ (`java -version`)  
- Apache Maven (`mvn -version`)  
- IDE (IntelliJ IDEA, VS Code, etc.)  
- (Optional) Postman for testing  
- (For Production) Access to a PostgreSQL instance  

---

### 🛠 Installation & Configuration

Clone the repository:
```bash
git clone <your-repository-url>
cd order-service
```

#### Configuration Files
Located in `src/main/resources/`:

- `application.properties` – sets default profile to `dev`  
- `application-dev.properties` – H2 setup (no config needed)  
- `application-prod.properties` – PostgreSQL setup  

> ⚠️ **Important:** In production, never hardcode credentials.  
> Use environment variables or system properties for:
> ```
> spring.datasource.username=${DB_USERNAME}
> spring.datasource.password=${DB_PASSWORD}
> ```

---

## ▶️ 5. Running the Application

### Option 1: Startup Script (Linux/macOS)
```bash
chmod +x startup.sh
./startup.sh
```

### Option 2: Using Maven Directly
```bash
mvn spring-boot:run
```

App starts at: [http://localhost:8080](http://localhost:8080)

---

### Running with Production Profile

#### 1. Package the Application
```bash
mvn clean package
```

Creates `target/order-service-0.0.1-SNAPSHOT.jar`.

#### 2. Set Environment Variables
```bash
export DB_USERNAME=your_prod_db_user
export DB_PASSWORD=your_prod_db_password
```

#### 3. Run with `prod` Profile
```bash
java -jar target/order-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

---

## 📚 6. Using the API

### Swagger UI
Visit: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### H2 Database Console (Dev Only)
- **URL:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
- **JDBC URL:** `jdbc:h2:mem:melidb`  
- **Username:** `sa`  
- **Password:** `password`

### Postman Collection
Import `postman_collection.json` (included in project root).

---

## 🌐 7. API Endpoints

**Base Path:** `/api/v1`

| Method | Endpoint | Description | Request Body | Success Response |
|---------|-----------|--------------|---------------|------------------|
| GET | `/` | Health Check | N/A | `200 OK (JSON)` |
| POST | `/orders` | Create new order | `CreateOrderRequest` | `201 Created (Order)` |
| GET | `/orders` | Get all active orders | N/A | `200 OK (List<Order>)` |
| GET | `/orders/{id}` | Get order by UUID | N/A | `200 OK (Order)` |
| DELETE | `/orders/{id}` | Soft-delete order | N/A | `204 No Content` |

---

### 🧾 Example: Create Order (`POST /api/v1/orders`)

**Request Body (JSON):**
```json
{
  "createdBy": "d290f1ee-6c54-4b01-90e6-d701748f0851",
  "items": [
    {
      "productId": "a1b2c3d4-e5f6-1234-abcd-556677889900",
      "productName": "Teclado Mecánico Gamer RGB",
      "quantity": 1,
      "pricePerUnit": 1899.99
    },
    {
      "productId": "f1e2d3c4-b5a6-4321-fedc-112233445566",
      "productName": "Mouse Inalámbrico Ergonómico",
      "quantity": 1,
      "pricePerUnit": 850.50
    }
  ]
}
```

**Successful Response (201 Created):**
```json
{
  "id": "c7a8b6e5-4d3c-4b2a-8f1e-9d0c1b2a3f4d",
  "createdBy": "d290f1ee-6c54-4b01-90e6-d701748f0851",
  "items": [
    {
      "id": "e1f2a3b4-c5d6-7890-1234-abcdef123456",
      "productId": "a1b2c3d4-e5f6-1234-abcd-556677889900",
      "productName": "Teclado Mecánico Gamer RGB",
      "quantity": 1,
      "pricePerUnit": 1899.99,
      "totalPrice": 1899.99
    },
    {
      "id": "f6e5d4c3-b2a1-0987-6543-fedcba987654",
      "productId": "f1e2d3c4-b5a6-4321-fedc-112233445566",
      "productName": "Mouse Inalámbrico Ergonómico",
      "quantity": 1,
      "pricePerUnit": 850.50,
      "totalPrice": 850.50
    }
  ],
  "totalPrice": 2750.49,
  "status": "PENDING",
  "deletedAt": null,
  "orderDate": "2025-10-16T19:10:00.123456",
  "lastUpdatedDate": "2025-10-16T19:10:00.123456"
}
```

---

### 🔍 Example: Get Order by ID (`GET /api/v1/orders/{id}`)

Replace `{id}` with a valid Order UUID.

**Success (200 OK):**  
Returns a full JSON object of the order (same as above).

**Failure (404 Not Found):**
```json
{
  "timestamp": "2025-10-16T19:35:00.987654",
  "status": 404,
  "error": "Not Found",
  "message": "Order not found with id: c7a8b6e5-4d3c-4b2a-8f1e-9d0c1b2a3f4d"
}
```

---

## Postman documentation

POST
<img width="1547" height="957" alt="Image" src="https://github.com/user-attachments/assets/55623dfd-fd88-4feb-8217-942f1dc58fe8" />

GET 
<img width="1538" height="927" alt="Image" src="https://github.com/user-attachments/assets/dfe7614c-6253-4322-955e-eee3054fa6dd" />

DELETE
<img width="1506" height="628" alt="Image" src="https://github.com/user-attachments/assets/268e8f22-a331-4918-81fa-8bb31c3a3572" />

POSTMAN JSON in
https://github.com/JaredTrOr/meli-ecommerce-orders-api/blob/master/MeliECommerce.postman_collection.json
