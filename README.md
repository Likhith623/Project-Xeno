# Project Xeno: AI-Native CRM Backend - Ultimate API Contract

Welcome to the backend repository of **Project Xeno**. This is an AI-Native Mini CRM designed to intelligently group shoppers and dispatch optimized communications via multiple channels using a Sovereign AI Agent.

This README serves as the **Exhaustive Frontend Integration Guide**. It contains API contracts for **EVERY SINGLE ENDPOINT** present in the codebase.

---

## 🌍 Base URL & Authentication

**Live Production URL:** `https://project-xeno.onrender.com/api/v1`
**Local Development URL:** `http://localhost:8080/api/v1`

**Authentication Header:**
Every request to the API must include the following header:
```http
X-API-KEY: likhit@178926a
Content-Type: application/json
```

**Common Response Wrapper (Applies to all endpoints):**
```json
{
  "success": true,
  "data": { ... }, // Or Array [...]
  "message": "Optional message",
  "errorCode": null,
  "errorMessage": null,
  "pagination": {
    "pageNumber": 0,
    "pageSize": 20,
    "totalElements": 1,
    "totalPages": 1,
    "last": true
  } // Nullable if not paginated
}
```

---

## 🤖 1. Agent API (`/agent`)

### `POST /chat`
*Starts the autonomous AI campaign generation process.*
* **Input:** `{"prompt": "string"}`
* **Output:** `{"sessionId": "UUID", "status": "IN_PROGRESS", "textReply": "string"}`

### `GET /sessions/{id}`
*Polls the session for completion. Wait for `status` = `COMPLETED`.*
* **Output:** 
  ```json
  {
    "id": "UUID",
    "goal": "string",
    "status": "COMPLETED",
    "plan": {
      "segmentId": "UUID",
      "campaignId": "UUID",
      "filterJson": { ... },
      "channelRecommendation": { "channel": "EMAIL", "estimatedOpenRate": "0.45", "estimatedCtr": "0.1" }
    }
  }
  ```

### `GET /sessions/{id}/decisions`
*Retrieves the internal step-by-step logic the AI took.*
* **Output:** Array of `{"stepOrder": int, "decisionType": "string", "reasoning": "string"}`

---

## 👥 2. Customers API (`/customers`)

### `POST /`
*Creates a customer.*
* **Input:** `{"name": "string", "email": "string", "phone": "string", "preferredChannel": "EMAIL", "tags": ["string"]}`
* **Output:** Customer Entity

### `POST /bulk`
*Ingests multiple customers.*
* **Input:** Array of Customer Input Objects
* **Output:** Array of Customer Entities

### `PUT /{id}`
*Updates a customer.*
* **Input:** Same as POST
* **Output:** Customer Entity

### `GET /`
*Fetches paginated list of customers.*
* **Query Params:** `page` (default 0), `size` (default 20)

### `GET /{id}`
*Fetches a single customer by ID.*

### `GET /{id}/360`
*Fetches the 360-degree RFM view of a customer.*
* **Output:** `{"id": "UUID", "name": "string", "rfmScore": float, "clvPredicted": float, "emailOpenRate": float}`

### `DELETE /{id}`
*Soft deletes a customer.*

### `GET /by-email?email={email}`
*Finds a customer by exact email.*

### `GET /by-tag?tag={tag}`
*Finds customers containing a specific tag.*

### `GET /{id}/orders`
*Fetches all orders for this customer.*

---

## 📦 3. Products API (`/products`)

### `POST /`
*Creates a product.*
* **Input:** `{"sku": "string", "name": "string", "price": float, "currency": "string"}`
* **Output:** Product Entity

### `POST /bulk`
*Bulk ingests products.*
* **Input:** Array of Product Input Objects

### `GET /`
*Fetches paginated products.*

### `GET /{id}`
*Fetches specific product.*

### `GET /categories`
*Fetches product categories (extracted from product names/tags).*

---

## 🛒 4. Orders API (`/orders`)

### `POST /`
*Ingests an order and triggers revenue attribution.*
* **Input:** 
  ```json
  {
    "customerId": "UUID",
    "orderNumber": "string",
    "totalAmount": float,
    "currency": "string",
    "items": [
      { "productId": "UUID", "productName": "string", "quantity": int, "unitPrice": float }
    ]
  }
  ```
* **Output:** Order Entity

### `POST /bulk`
*Bulk ingests orders.*
* **Input:** Array of Order Input Objects

### `GET /`
*Paginated orders list.*

### `GET /{id}`
*Specific order.*

---

## 🎯 5. Segments API (`/segments`)

### `POST /`
*Manually create a segment.*
* **Input:** `{"name": "string", "description": "string", "type": "DYNAMIC", "filterSql": "SELECT id FROM customers..."}`

### `GET /`
*Paginated segments list.*

### `GET /{id}`
*Specific segment details.*

### `PATCH /{id}`
*Update segment fields.*
* **Input:** `{"name": "string", "description": "string"}`

### `GET /{id}/members`
*Fetch customers who belong to this segment based on the last evaluation.*

### `DELETE /{id}`
*Delete segment.*

### `POST /{id}/evaluate`
*Force execution of the `filterSql` against the DB to refresh segment members.*
* **Output:** Integer (Count of matched members)

---

## 🚀 6. Campaigns API (`/campaigns`)

### `POST /`
*Create a campaign manually.*
* **Input:** `{"name": "string", "segmentId": "UUID", "goal": "string"}`

### `GET /`
*Paginated campaigns.*

### `GET /{id}`
*Specific campaign (includes totalSent, totalOpened, revenueAttributed).*

### `POST /{id}/execute`
*Launch the campaign. Dispatches messages to the channel stub.*

### `PATCH /{id}/status?status={status}`
*Manually update status (DRAFT, SCHEDULED, RUNNING, PAUSED, COMPLETED, CANCELLED).*
* **Query Params:** `status`

### `GET /{id}/performance`
*Get aggregated metrics.*
* **Output:** `{"totalSent": int, "totalDelivered": int, "totalOpened": int, "revenueAttributed": float, ...}`

### `GET /opt-out-alerts`
*Fetches campaigns hitting high opt-out thresholds.*

### `GET /{id}/variants/mab-stats`
*Shortcut to fetch MAB variant stats for this campaign.*

### `GET /{id}/corrections`
*Fetches auto-corrections triggered by the AI monitor for this campaign.*

### `POST /{id}/simulate`
*Triggers a dry-run Monte Carlo simulation for this campaign.*

---

## 🎨 7. Variants API (`/variants`)

### `POST /`
*Create a message variant.*
* **Input:** `{"campaignId": "UUID", "name": "string", "channel": "EMAIL", "subjectLine": "string", "bodyHtml": "string"}`

### `GET /campaign/{campaignId}`
*Get all variants for a campaign.*

### `GET /{id}`
*Specific variant.*

### `PATCH /{id}`
*Update variant content.*
* **Input:** Partial Variant Object.

### `DELETE /{id}`
*Delete variant.*

### `GET /{campaignId}/mab-stats`
*Get Multi-Armed Bandit stats.*
* **Output:** Array of `{"variantId": "UUID", "impressions": int, "conversions": int, "conversionRate": float, "thompsonSample": float}`

---

## 📡 8. Communications API (`/communications`)

### `GET /campaign/{campaignId}`
*Fetch all messages sent out for a specific campaign.*
* **Output:** Array of Communication Entities (`status`, `channel`, `recipientAddress`).

### `GET /customer/{customerId}`
*Fetch the timeline of messages sent to a customer.*

### `PATCH /{id}/status?status={status}`
*Manually overwrite a communication's status (SENT, DELIVERED, OPENED, CLICKED, CONVERTED).*

---

## 🔄 9. Callbacks API (`/callbacks/channel`)

### `POST /`
*Webhook receiver for the Channel Stub.*
* **Input:** `{"channelMessageId": "string", "status": "DELIVERED|OPENED|CLICKED|FAILED", "metadata": {}}`

---

## 🧠 10. Memory API (`/memory`)

### `GET /`
*Fetches all learned Org Memory Entries (e.g. "Email works best on Fridays").*

### `GET /query?tag={tag}`
*Query organizational memory by segment tag.*

---

## 📊 11. Simulation API (`/simulations`)

### `POST /`
*Run an isolated audience simulation.*
* **Input:** `{"campaignId": "UUID", "audienceSize": int}`

### `POST /campaigns/{id}/simulate`
*Shortcut simulation trigger for a campaign.*
* **Input:** `{"audienceSize": int}`

### `GET /{id}`
*Get simulation results.*
* **Output:** `{"status": "COMPLETED", "projectedOpens": int, "projectedClicks": int}`

---

## 🔧 12. Corrections API (`/corrections`)

### `GET /`
*Fetches all AI-triggered course corrections (e.g. paused campaigns due to high bounce rates).*
* **Output:** Array of `{"triggerType": "string", "actionTaken": "string", "aiReasoning": "string"}`

---

## 📋 13. Audit Logs API (`/audit-logs`)

### `GET /entity/{entityType}/{entityId}`
*Fetches immutable audit history for an entity.*
* **Output:** Array of `{"action": "CREATE|UPDATE|DELETE", "timestamp": "date", "changes": {}}`

### `GET /trace/{traceId}`
*Fetches logs by correlation trace ID.*

### `GET /actor/{actorId}`
*Fetches logs by system/user actor.*

---

## 💡 Frontend Integration Pro-Tip
Use `POST /agent/chat` -> `GET /agent/sessions/{id}` to automatically assemble `Segments`, `Campaigns`, and `Variants` without having to build massive multi-step forms. Once the agent gives you the `campaignId`, call `POST /campaigns/{id}/execute` to launch the dispatch sequence, and visualize real-time tracking via `GET /campaigns/{id}/performance`.
