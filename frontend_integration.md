# Project Xeno: AI-Native CRM Backend - Ultimate API Contract

Welcome to the **100% Exhaustively Generated Frontend Integration Guide**.
This file guarantees 100% parity with the backend source code.

## 🌍 Base URL & Authentication
**Live Production URL:** `https://project-xeno.onrender.com/api/v1`
**Authentication Header:** `X-API-KEY: likhit@178926a`

---

## Audience Simulator

### `POST /api/v1/simulations`
**Summary:** Trigger a new audience simulation

**Payload (`application/json`):**
```json
{
  "campaignId": "string",
  "syntheticAudienceSize": 0,
  "personaDistribution": {
    "key": {}
  }
}
```

**Responses:**
- `200`: OK
---

### `POST /api/v1/simulations/campaigns/{id}/simulate`
**Summary:** Trigger a simulation for a specific campaign

**Parameters:**
- `id`* (path): `string`

**Payload (`application/json`):**
```json
{
  "campaignId": "string",
  "syntheticAudienceSize": 0,
  "personaDistribution": {
    "key": {}
  }
}
```

**Responses:**
- `200`: OK
---

### `POST /api/v1/simulations/campaigns/{id}/counterfactual`
**Summary:** Run a counterfactual simulation for a campaign

**Parameters:**
- `id`* (path): `string`
- `channel`* (query): `string`
- `counterfactualService`* (query): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/simulations/{id}`
**Summary:** Get simulation run result

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

## Audit Logs

### `GET /api/v1/audit-logs/trace/{traceId}`
**Summary:** Get audit logs by trace ID

**Parameters:**
- `traceId`* (path): `string`
- `pageable`* (query): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/audit-logs/entity/{entityType}/{entityId}`
**Summary:** Get audit logs by entity type and ID

**Parameters:**
- `entityType`* (path): `string`
- `entityId`* (path): `string`
- `pageable`* (query): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/audit-logs/actor/{actorId}`
**Summary:** Get audit logs by actor ID

**Parameters:**
- `actorId`* (path): `string`
- `pageable`* (query): `string`

**Responses:**
- `200`: OK
---

## Campaign

### `GET /api/v1/campaigns`
**Summary:** Get all campaigns with pagination

**Parameters:**
- `page` (query): `integer`
- `size` (query): `integer`

**Responses:**
- `200`: OK
---

### `POST /api/v1/campaigns`
**Summary:** Create a new campaign manually

**Payload (`application/json`):**
```json
{
  "name": "string",
  "description": "string",
  "segmentId": "string",
  "status": "DRAFT",
  "goal": "string",
  "scheduledAt": "2026-06-13T12:00:00Z",
  "timezone": "string",
  "maxSendCount": 0,
  "optOutRateThreshold": 0.0
}
```

**Responses:**
- `200`: OK
---

### `POST /api/v1/campaigns/{id}/simulate`
**Summary:** Trigger a simulation for a specific campaign

**Parameters:**
- `id`* (path): `string`

**Payload (`application/json`):**
```json
{
  "campaignId": "string",
  "syntheticAudienceSize": 0,
  "personaDistribution": {
    "key": {}
  }
}
```

**Responses:**
- `200`: OK
---

### `POST /api/v1/campaigns/{id}/execute`
**Summary:** Trigger async execution of a campaign

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

### `POST /api/v1/campaigns/{id}/approve`
**Summary:** Approve an AI proposed campaign and execute it

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

### `PATCH /api/v1/campaigns/{id}/status`
**Summary:** Update campaign status (pause, cancel, etc.)

**Parameters:**
- `id`* (path): `string`

**Payload (`application/json`):**
```json
{
  "status": "DRAFT"
}
```

**Responses:**
- `200`: OK
---

### `GET /api/v1/campaigns/{id}`
**Summary:** Get campaign details and metrics

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/campaigns/{id}/variants/mab-stats`
**Summary:** Get Thompson Sampling (MAB) statistics for campaign variants

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/campaigns/{id}/timeline`
**Summary:** Get a chronological narrative timeline of the campaign

**Parameters:**
- `id`* (path): `string`
- `timelineService`* (query): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/campaigns/{id}/performance`
**Summary:** Get campaign performance metrics including delivery, open, and conversion rates

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/campaigns/{id}/corrections`
**Summary:** Get self-correction events for a specific campaign

**Parameters:**
- `id`* (path): `string`
- `page` (query): `integer`
- `size` (query): `integer`

**Responses:**
- `200`: OK
---

### `GET /api/v1/campaigns/{id}/analytics/narrative`
**Summary:** Get a natural language narrative analysis of the campaign's performance

**Parameters:**
- `id`* (path): `string`
- `analyticsService`* (query): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/campaigns/proposals`
**Summary:** Get all autonomous AI campaign proposals awaiting human approval (Tinder Swipe UI)

**Parameters:**
- `page` (query): `integer`
- `size` (query): `integer`

**Responses:**
- `200`: OK
---

### `GET /api/v1/campaigns/opt-out-alerts`
**Summary:** Get opt-out alerts for running campaigns exceeding safety thresholds

**Responses:**
- `200`: OK
---

## Channel Callbacks

### `POST /api/v1/callbacks/channel`
**Summary:** Receive channel callback webhook

**Payload (`application/json`):**
```json
{
  "channelMessageId": "string",
  "communicationId": "string",
  "eventType": "DELIVERED",
  "payload": {
    "key": {}
  }
}
```

**Responses:**
- `200`: OK
---

## Communication

### `PATCH /api/v1/communications/{id}/status`
**Summary:** Update status of a communication

**Parameters:**
- `id`* (path): `string`
- `status`* (query): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/communications/customer/{customerId}`
**Summary:** Get communications for a customer

**Parameters:**
- `customerId`* (path): `string`
- `pageable`* (query): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/communications/campaign/{campaignId}`
**Summary:** Get communications for a campaign

**Parameters:**
- `campaignId`* (path): `string`
- `pageable`* (query): `string`

**Responses:**
- `200`: OK
---

## Customer

### `GET /api/v1/customers/{id}`
**Summary:** Get basic customer details

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

### `PUT /api/v1/customers/{id}`
**Summary:** Update an existing customer

**Parameters:**
- `id`* (path): `string`

**Payload (`application/json`):**
```json
{
  "email": "string",
  "phone": "string",
  "whatsappNumber": "string",
  "name": "string",
  "gender": "MALE",
  "dateOfBirth": "string",
  "city": "string",
  "state": "string",
  "country": "string",
  "tags": [
    "string"
  ],
  "customAttributes": {
    "key": {}
  },
  "preferredChannel": "EMAIL",
  "optOutChannels": [
    "string"
  ],
  "isGloballyOptedOut": false
}
```

**Responses:**
- `200`: OK
---

### `DELETE /api/v1/customers/{id}`
**Summary:** Delete a customer

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/customers`
**Summary:** Get all customers with pagination

**Parameters:**
- `page` (query): `integer`
- `size` (query): `integer`

**Responses:**
- `200`: OK
---

### `POST /api/v1/customers`
**Summary:** Create a new customer

**Payload (`application/json`):**
```json
{
  "externalId": "string",
  "email": "string",
  "phone": "string",
  "whatsappNumber": "string",
  "name": "string",
  "gender": "MALE",
  "dateOfBirth": "string",
  "city": "string",
  "state": "string",
  "country": "string",
  "tags": [
    "string"
  ],
  "customAttributes": {
    "key": {}
  },
  "preferredChannel": "EMAIL",
  "optOutChannels": [
    "string"
  ],
  "globallyOptedOut": false
}
```

**Responses:**
- `200`: OK
---

### `POST /api/v1/customers/bulk`
**Summary:** Create multiple customers in bulk

**Payload (`application/json`):**
```json
[
  {
    "externalId": "string",
    "email": "string",
    "phone": "string",
    "whatsappNumber": "string",
    "name": "string",
    "gender": "MALE",
    "dateOfBirth": "string",
    "city": "string",
    "state": "string",
    "country": "string",
    "tags": [
      "string"
    ],
    "customAttributes": {
      "key": {}
    },
    "preferredChannel": "EMAIL",
    "optOutChannels": [
      "string"
    ],
    "globallyOptedOut": false
  }
]
```

**Responses:**
- `200`: OK
---

### `GET /api/v1/customers/{id}/orders`
**Summary:** Get customer orders

**Parameters:**
- `id`* (path): `string`
- `page` (query): `integer`
- `size` (query): `integer`

**Responses:**
- `200`: OK
---

### `GET /api/v1/customers/{id}/360`
**Summary:** Get customer 360 view including metrics

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/customers/by-tag`
**Summary:** Get customers by tag

**Parameters:**
- `tag`* (query): `string`
- `page` (query): `integer`
- `size` (query): `integer`

**Responses:**
- `200`: OK
---

### `GET /api/v1/customers/by-email`
**Summary:** No summary

**Parameters:**
- `email`* (query): `string`

**Responses:**
- `200`: OK
---

## Order

### `GET /api/v1/orders`
**Summary:** Get all orders with pagination

**Parameters:**
- `page` (query): `integer`
- `size` (query): `integer`

**Responses:**
- `200`: OK
---

### `POST /api/v1/orders`
**Summary:** Create a new order

**Payload (`application/json`):**
```json
{
  "customerId": "string",
  "orderNumber": "string",
  "status": "PENDING",
  "channel": "string",
  "totalAmount": 0.0,
  "currency": "string",
  "discountAmount": 0.0,
  "couponCode": "string",
  "placedAt": "2026-06-13T12:00:00Z",
  "metadata": {
    "key": {}
  },
  "items": [
    {
      "productId": "string",
      "productSku": "string",
      "productName": "string",
      "quantity": 0,
      "unitPrice": 0.0,
      "discountAmount": 0.0,
      "lineTotal": 0.0
    }
  ]
}
```

**Responses:**
- `200`: OK
---

### `POST /api/v1/orders/bulk`
**Summary:** Create multiple orders in bulk

**Payload (`application/json`):**
```json
[
  {
    "customerId": "string",
    "orderNumber": "string",
    "status": "PENDING",
    "channel": "string",
    "totalAmount": 0.0,
    "currency": "string",
    "discountAmount": 0.0,
    "couponCode": "string",
    "placedAt": "2026-06-13T12:00:00Z",
    "metadata": {
      "key": {}
    },
    "items": [
      {
        "productId": "string",
        "productSku": "string",
        "productName": "string",
        "quantity": 0,
        "unitPrice": 0.0,
        "discountAmount": 0.0,
        "lineTotal": 0.0
      }
    ]
  }
]
```

**Responses:**
- `200`: OK
---

### `GET /api/v1/orders/{id}`
**Summary:** Get order details

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

## Organizational Memory

### `GET /api/v1/memory`
**Summary:** Get all organizational memory entries

**Parameters:**
- `pageable`* (query): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/memory/query`
**Summary:** Query organizational memory by segment tag and channel

**Parameters:**
- `segmentTag` (query): `string`
- `channel` (query): `string`
- `pageable`* (query): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/memory/ask`
**Summary:** Ask the organizational memory a question using natural language

**Parameters:**
- `query`* (query): `string`
- `llmGatewayService`* (query): `string`

**Responses:**
- `200`: OK
---

## Product

### `GET /api/v1/products`
**Summary:** Get all products with pagination

**Parameters:**
- `page` (query): `integer`
- `size` (query): `integer`

**Responses:**
- `200`: OK
---

### `POST /api/v1/products`
**Summary:** Create a new product

**Payload (`application/json`):**
```json
{
  "sku": "string",
  "name": "string",
  "categoryId": "string",
  "price": 0.0,
  "currency": "string",
  "brand": "string",
  "tags": [
    "string"
  ],
  "attributes": {
    "key": {}
  },
  "active": false
}
```

**Responses:**
- `200`: OK
---

### `POST /api/v1/products/bulk`
**Summary:** Create multiple products in bulk

**Payload (`application/json`):**
```json
[
  {
    "sku": "string",
    "name": "string",
    "categoryId": "string",
    "price": 0.0,
    "currency": "string",
    "brand": "string",
    "tags": [
      "string"
    ],
    "attributes": {
      "key": {}
    },
    "active": false
  }
]
```

**Responses:**
- `200`: OK
---

### `GET /api/v1/products/{id}`
**Summary:** Get product details

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/products/categories`
**Summary:** Get all product categories

**Responses:**
- `200`: OK
---

## Segment

### `GET /api/v1/segments`
**Summary:** Get all segments with pagination

**Parameters:**
- `page` (query): `integer`
- `size` (query): `integer`

**Responses:**
- `200`: OK
---

### `POST /api/v1/segments`
**Summary:** Create a new segment

**Payload (`application/json`):**
```json
{
  "name": "string",
  "description": "string",
  "type": "STATIC",
  "status": "DRAFT",
  "filterSql": "string",
  "filterJson": {
    "key": {}
  },
  "isPinned": false,
  "createdByAgent": false,
  "agentGoal": "string"
}
```

**Responses:**
- `200`: OK
---

### `POST /api/v1/segments/{id}/evaluate`
**Summary:** Trigger async evaluation of a segment

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/segments/{id}`
**Summary:** Get segment details

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

### `DELETE /api/v1/segments/{id}`
**Summary:** Delete a segment

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

### `PATCH /api/v1/segments/{id}`
**Summary:** Update segment details

**Parameters:**
- `id`* (path): `string`

**Payload (`application/json`):**
```json
{
  "name": "string",
  "description": "string",
  "filterSql": "string",
  "filterJson": {
    "key": {}
  },
  "isPinned": false
}
```

**Responses:**
- `200`: OK
---

### `GET /api/v1/segments/{id}/persona`
**Summary:** Generate an AI persona for this segment

**Parameters:**
- `id`* (path): `string`
- `personaGenerationService`* (query): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/segments/{id}/members`
**Summary:** Get paginated list of customer IDs in a segment

**Parameters:**
- `id`* (path): `string`
- `page` (query): `integer`
- `size` (query): `integer`

**Responses:**
- `200`: OK
---

## Self-Correction Engine

### `GET /api/v1/corrections`
**Summary:** Get all correction events

**Parameters:**
- `pageable`* (query): `string`

**Responses:**
- `200`: OK
---

## Sovereign Agent

### `POST /api/v1/agent/chat`
**Summary:** Send a prompt to the sovereign agent

**Payload (`application/json`):**
```json
{
  "prompt": "string",
  "sessionId": "string"
}
```

**Responses:**
- `200`: OK
---

### `GET /api/v1/agent/sessions/{id}`
**Summary:** Poll the status and plan of a sovereign agent session

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/agent/sessions/{id}/decisions`
**Summary:** Get the full ReAct reasoning chain (decision audit trail) for a session

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

## Variant

### `POST /api/v1/variants`
**Summary:** Create a new variant for a campaign

**Payload (`application/json`):**
```json
{
  "campaignId": "string",
  "name": "string",
  "channel": "email",
  "subjectLine": "string",
  "previewText": "string",
  "bodyText": "string",
  "bodyHtml": "string",
  "ctaText": "string",
  "ctaUrl": "string",
  "mediaUrl": "string",
  "templateId": "string",
  "templateParams": {
    "key": {}
  },
  "generatedByAi": false,
  "generationPrompt": "string"
}
```

**Responses:**
- `200`: OK
---

### `GET /api/v1/variants/{id}`
**Summary:** Get variant by ID

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

### `DELETE /api/v1/variants/{id}`
**Summary:** Soft delete variant

**Parameters:**
- `id`* (path): `string`

**Responses:**
- `200`: OK
---

### `PATCH /api/v1/variants/{id}`
**Summary:** Update variant details

**Parameters:**
- `id`* (path): `string`

**Payload (`application/json`):**
```json
{
  "name": "string",
  "channel": "string",
  "subjectLine": "string",
  "previewText": "string",
  "bodyText": "string",
  "bodyHtml": "string",
  "ctaText": "string",
  "ctaUrl": "string",
  "mediaUrl": "string",
  "templateId": "string",
  "templateParams": {
    "key": {}
  },
  "mabIsActive": false
}
```

**Responses:**
- `200`: OK
---

### `GET /api/v1/variants/{campaignId}/mab-stats`
**Summary:** Get Thompson Sampling (MAB) statistics for campaign variants

**Parameters:**
- `campaignId`* (path): `string`

**Responses:**
- `200`: OK
---

### `GET /api/v1/variants/campaign/{campaignId}`
**Summary:** Get all variants for a campaign

**Parameters:**
- `campaignId`* (path): `string`

**Responses:**
- `200`: OK
---

## agent-decision-controller

### `GET /api/v1/agent/sessions/{sessionId}/decisions`
**Summary:** No summary

**Parameters:**
- `sessionId`* (path): `string`

**Responses:**
- `200`: OK
---

## ai-testing-controller

### `POST /api/v1/test/agi/trigger-war-room`
**Summary:** No summary

**Payload (`application/json`):**
```json
{
  "key": "string"
}
```

**Responses:**
- `200`: OK
---

### `POST /api/v1/test/agi/trigger-omni-awareness`
**Summary:** No summary

**Responses:**
- `200`: OK
---

### `POST /api/v1/test/agi/trigger-fund-manager`
**Summary:** No summary

**Responses:**
- `200`: OK
---


---

# 🧠 Core AI Features & Business Logic Guide
*This section provides the conceptual frontend integration logic for all AI Features.*

## 1. The Sovereign AI Agent (`AgentController`)
The Sovereign Agent allows users to input natural language goals (e.g., "Win back churned VIP users"). 
- **Endpoint:** `POST /api/v1/agent/chat`
- **Frontend Flow:** The frontend should display a chat-like interface. When the user submits a goal, call this endpoint. It returns a `sessionId`.
- **Polling:** Use `GET /api/v1/agent/sessions/{sessionId}` to poll the agent's progress. Display a loading animation. Once `status` becomes `COMPLETED`, display the `plan` JSON which contains the IDs of the newly created Segments and Campaigns.

## 2. Multi-Armed Bandit (MAB) & Thompson Sampling (`CampaignController`)
- **Endpoint:** `GET /api/v1/campaigns/{id}/variants/mab-stats`
- **Frontend Flow:** When viewing an active campaign, the frontend should display a dashboard showing real-time variant performance. Use this endpoint to fetch the `conversionRate`, `weight`, and `impressions` of each variant (A/B/C/D). Render this as a dynamic pie chart or bar chart showing the AI dynamically shifting traffic to the winning variant.

## 3. Autonomous Campaign Proposals ("Tinder-style UI")
- **Endpoint:** `GET /api/v1/campaigns/proposals`
- **Frontend Flow:** The backend proactively generates highly optimized campaigns at midnight. The frontend should fetch these proposals and display them in a card-stack UI. The user can review the AI's proposal, and click "Approve". 
- **Action:** Clicking "Approve" calls `POST /api/v1/campaigns/{id}/approve` to instantly activate the campaign.

## 4. The Sleep Agent (Omni-Awareness Fatigue)
- **Frontend Flow:** When creating a campaign, the frontend doesn't need to manually check if users are fatigued. The backend automatically queries `channel_cooldown_until` in the `CustomerEntity`. The frontend just needs to display a stat on the Campaign Dashboard: "Users Suppressed by Fatigue AI: X". 

## 5. Organizational Memory (`MemoryController`)
- **Endpoint:** `GET /api/v1/memory`
- **Frontend Flow:** The backend learns from past campaign failures and successes. The frontend should have a "Brain" or "Learnings" tab. Fetch the memory logs and display them as insights (e.g., "Urgency CTAs perform 22% better on Friday afternoons"). 

## 6. Real-Time AGI Testing Suite (`AITestingController`)
- **Frontend Flow:** Build a "Developer / Test" panel in the frontend where the user can click buttons to instantly trigger the backend cron jobs:
  - Trigger War Room (Multi-Agent Debate)
  - Trigger Fund Manager (Budget Reallocation)
  - Trigger Omni-Awareness (Fatigue Rules)
- Use `POST /api/v1/test/agi/*` endpoints for these buttons. 

---
**End of Ultimate Guide**
