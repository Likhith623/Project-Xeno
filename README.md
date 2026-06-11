# Project Xeno - Complete Backend Documentation

This document serves as the comprehensive guide to the **Xenon Backend Architecture**, outlining all implemented domains, their purpose, the database tables they modify, and the Swagger UI configurations for testing. 

## 📌 Testing via Swagger UI

Instead of using Postman for manual testing, **Swagger UI** has been fully integrated into the project. It provides an interactive interface to view all available endpoints, see their expected request payloads, and test them directly.

**How to Access:**
1. Start the Spring Boot application.
2. Open your browser and navigate to: `http://localhost:8080/swagger-ui/index.html` (or the respective port you configure).
3. The UI will list all domains and endpoints grouped by tags.

---

## 🏗️ Domain Functionalities & Endpoints

### 1. 👥 Customer Domain
**Purpose:** Manages the core entity of the CRM — the customers. It handles their creation, metric aggregation, and lifecycle.
*   **Table Modified:** `customers`, `customer_metrics`
*   **Endpoints:**
    *   `POST /api/v1/customers` - Create a new customer.
        *   **Test Body:** `{"name": "John Doe", "email": "john@example.com", "phone": "1234567890"}`
    *   `GET /api/v1/customers/{id}` - Fetch a specific customer by ID.

### 2. 🛍️ Product Domain
**Purpose:** Handles product catalog and categories. Essential for linking orders and campaigns to specific items.
*   **Table Modified:** `products`, `product_categories`
*   **Endpoints:**
    *   `POST /api/v1/products/categories` - Create a category.
        *   **Test Body:** `{"name": "Electronics", "description": "Gadgets"}`
    *   `POST /api/v1/products` - Create a new product.
        *   **Test Body:** `{"sku": "SKU-123", "name": "Laptop", "price": 999.99, "categoryId": "UUID"}`
    *   `GET /api/v1/products/{id}` - Get a product by ID.

### 3. 🛒 Order Domain
**Purpose:** Processes transactions and customer purchases. Tracks exactly what products a customer bought.
*   **Table Modified:** `orders`, `order_items`
*   **Endpoints:**
    *   `POST /api/v1/orders` - Place a new order.
        *   **Test Body:** `{"customerId": "UUID", "totalAmount": 999.99, "items": [{"productId": "UUID", "quantity": 1, "unitPrice": 999.99}]}`
    *   `GET /api/v1/orders/customer/{customerId}` - Get all orders for a specific customer.

### 4. 🎯 Segment Domain
**Purpose:** Allows dynamic grouping of customers based on rules and criteria (e.g., "Customers who spent > $500").
*   **Table Modified:** `segments`
*   **Endpoints:**
    *   `POST /api/v1/segments` - Create a new segment.
        *   **Test Body:** `{"name": "High Spenders", "criteria": {"totalSpent": {"$gt": 500}}}`
    *   `POST /api/v1/segments/{id}/evaluate` - Triggers asynchronous evaluation of the segment against all customers.

### 5. 📢 Campaign Domain
**Purpose:** Handles marketing campaigns, linking segments to promotional activities, and tracking their overall metrics.
*   **Table Modified:** `campaigns`, `campaign_metrics`
*   **Endpoints:**
    *   `POST /api/v1/campaigns` - Create a campaign.
        *   **Test Body:** `{"name": "Summer Sale", "segmentId": "UUID", "channel": "EMAIL"}`
    *   `POST /api/v1/campaigns/{id}/execute` - Triggers the campaign execution process asynchronously.

### 6. 🧪 Variant & Multi-Armed Bandit Domain
**Purpose:** Implements A/B testing and Bayesian inference (Thompson Sampling) for campaigns. Dynamically allocates traffic to the best-performing message variant.
*   **Table Modified:** `variants`
*   **Endpoints:**
    *   `POST /api/v1/variants` - Create a variant for a campaign.
        *   **Test Body:** `{"campaignId": "UUID", "name": "Variant A", "content": "Huge sale today!"}`
    *   `POST /api/v1/variants/campaign/{campaignId}/select` - Uses the MAB algorithm to pick the best variant for the next user.
    *   `POST /api/v1/variants/{id}/track` - Track an outcome (e.g., success=true) to update the algorithm's learning.

### 7. ✉️ Communication Domain
**Purpose:** Logs all messages sent out (Emails, SMS, etc.) and tracks their status (Sent, Failed, Opened).
*   **Table Modified:** `communication_logs`
*   **Endpoints:**
    *   `GET /api/v1/communications/customer/{customerId}` - Get communication history for a customer.

### 8. 🎟️ Event Domain
**Purpose:** Ingests raw events from customer activity (e.g., Page Views, Clicks, Add to Cart).
*   **Table Modified:** `events`
*   **Endpoints:**
    *   `POST /api/v1/events` - Ingest an event.
        *   **Test Body:** `{"customerId": "UUID", "eventType": "PAGE_VIEW", "source": "WEB", "payload": {"url": "/home"}}`
    *   `GET /api/v1/events/customer/{customerId}` - Fetch a customer's event timeline.

### 9. ⚙️ Settings Domain
**Purpose:** Manages global system configurations and settings (e.g., feature flags, API keys).
*   **Table Modified:** `system_settings`
*   **Endpoints:**
    *   `POST /api/v1/settings` - Create or update a setting.
        *   **Test Body:** `{"key": "ENABLE_ML", "value": "true", "description": "Enable ML features"}`
    *   `GET /api/v1/settings` - Retrieve all settings.

### 10. 🧠 ML Domain
**Purpose:** Tracks and manages Machine Learning model training jobs and metrics.
*   **Table Modified:** `model_training_logs`
*   **Endpoints:**
    *   `POST /api/v1/ml/logs` - Start a training log.
        *   **Test Body:** `{"modelName": "ChurnPredictor", "modelVersion": "v1.0"}`
    *   `PATCH /api/v1/ml/logs/{id}/complete` - Mark a model as completed with metrics.

### 11. 📊 Report Domain
**Purpose:** Manages configurations for custom reports and dashboards built on JSON DSL queries.
*   **Table Modified:** `report_configs`
*   **Endpoints:**
    *   `POST /api/v1/reports/configs` - Create a report config.
        *   **Test Body:** `{"name": "Sales Q1", "queryDsl": "SELECT * FROM orders", "isActive": true}`

### 12. 🔐 Auth Domain
**Purpose:** Handles Role-Based Access Control (RBAC), mapping users to roles and permissions.
*   **Table Modified:** `users`, `roles`, `permissions`, `user_roles`, `role_permissions`
*   **Endpoints:**
    *   `POST /api/v1/users` - Create a new user.
        *   **Test Body:** `{"username": "admin", "email": "admin@xeno.com", "password": "SecurePassword123"}`
    *   `GET /api/v1/users/{id}` - Get a user profile.

### 13. 🪝 Webhook Domain
**Purpose:** Allows external systems to subscribe to internal events via HTTP callbacks. Logs execution statuses.
*   **Table Modified:** `webhook_configs`, `webhook_logs`
*   **Endpoints:**
    *   `POST /api/v1/webhooks/configs` - Register a new webhook.
        *   **Test Body:** `{"url": "https://example.com/hook", "eventType": "ORDER_CREATED", "isActive": true}`
    *   `POST /api/v1/webhooks/trigger/{eventType}` - Manually trigger webhooks.

### 14. 🕵️‍♂️ Audit Domain
**Purpose:** Maintains an immutable audit trail of critical actions performed in the system.
*   **Table Modified:** `audit_logs`
*   **Endpoints:**
    *   `POST /api/v1/audit` - Log a manual action.
        *   **Test Body:** `{"entityName": "Customer", "entityId": "UUID", "action": "UPDATE", "changes": {"old": "A", "new": "B"}}`
    *   `GET /api/v1/audit/{entityName}/{entityId}` - Fetch logs for a specific entity.

---

## 🚀 Key Architectural Highlights
*   **Database Schema:** Implements robust JSONB handling via Hypersistence Utils for NoSQL-like flexibility within a Relational Model.
*   **Concurrency:** Utilizes `@Async("taskExecutor")` to ensure that heavy processes (like evaluating segments or sending campaign blasts) run in the background, not blocking the main thread.
*   **Intelligence:** Built-in Thomson Sampling (`commons-math3` Bayesian Inference) for variant Multi-Armed Bandit selection to maximize campaign conversion rates over time. 
*   **MapStruct Mappers:** Employed for clean, performant separation between DTOs and internal Entities.

Enjoy building with Xeno!
