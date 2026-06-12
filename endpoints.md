# Xeno CRM - Comprehensive Endpoints Guide

Based on a rigorous analysis of all `@RestController` controllers in the Xeno CRM backend, here is the exhaustive list of every active API endpoint, its path, and its exact operational purpose.

---

## 1. Sovereign AI Agent (`/api/v1/agent`)
The core orchestrator for AI operations.
* **`POST /chat`**
  * **Purpose:** Main entry point for the Sovereign Agent. Takes a natural language request from the user (e.g., "Send a discount to VIPs"), communicates with Google Gemini, translates the intent into an SQL segment, drafts message variants, and returns an execution plan for human approval.
* **`GET /session/{sessionId}`**
  * **Purpose:** Retrieves the current state and conversation log of an active or completed AI session.
* **`GET /session/{sessionId}/decisions`**
  * **Purpose:** Retrieves a step-by-step breakdown of every decision made by the AI agent during a session for explainability and auditing.

## 2. Customers (`/api/v1/customers`)
Handles profile creation, tracking, and deep metrics.
* **`POST /`**
  * **Purpose:** Ingests a new customer profile into the CRM.
* **`POST /bulk`**
  * **Purpose:** Bulk ingest multiple customers at once. This supports fast onboarding of legacy CRM data into the system.
* **`GET /`**
  * **Purpose:** Retrieves a paginated list of all customers. Supports `page` and `size` query params.
* **`GET /{id}`**
  * **Purpose:** Fetches basic profile details of a specific customer by their UUID.
* **`GET /{id}/360`**
  * **Purpose:** Retrieves the "Customer 360" profile. This aggregates lifetime monetary spend, recent orders, churn probability, and AI-predicted CLV.
* **`PUT /{id}`**
  * **Purpose:** Updates existing fields on a customer profile.
* **`DELETE /{id}`**
  * **Purpose:** Hard deletes a customer record from the database.
* **`GET /by-email`**
  * **Purpose:** Looks up a specific customer's UUID by providing their email address via query param.
* **`GET /by-tag`**
  * **Purpose:** Retrieves a paginated list of customers that match a specific tag (e.g., "VIP", "ChurnRisk").
* **`GET /{id}/orders`**
  * **Purpose:** Retrieves a paginated list of all past orders placed by a specific customer.

## 3. Products (`/api/v1/products`)
Product catalog management.
* **`POST /`**
  * **Purpose:** Creates a new product in the CRM catalog.
* **`POST /bulk`**
  * **Purpose:** Bulk ingest multiple products at once. Used for importing entire catalogs.
* **`GET /`**
  * **Purpose:** Retrieves a paginated list of all products.
* **`GET /categories`**
  * **Purpose:** Retrieves a distinct list of all product categories currently available in the system.
* **`GET /{id}`**
  * **Purpose:** Fetches details for a specific product.

## 4. Orders (`/api/v1/orders`)
Transaction and revenue tracking.
* **`POST /`**
  * **Purpose:** Records a new transaction/order. Successfully inserting an order triggers asynchronous processes to recalculate the buyer's metrics (e.g., updating their monetary total in the Customer 360 view).
* **`POST /bulk`**
  * **Purpose:** Bulk ingest multiple past orders. Triggers metric recalculation for all associated customers.
* **`GET /`**
  * **Purpose:** Retrieves a paginated list of all orders.
* **`GET /{id}`**
  * **Purpose:** Fetches details for a specific order.

## 5. Audience Segments (`/api/v1/segments`)
Targeting rules and queries.
* **`POST /`**
  * **Purpose:** Creates a new audience segment manually (or programmatically by the AI) by providing a raw SQL query or JSON filter.
* **`GET /`**
  * **Purpose:** Retrieves a paginated list of all audience segments.
* **`GET /{id}`**
  * **Purpose:** Retrieves details about a specific segment.
* **`PATCH /{id}`**
  * **Purpose:** Updates the segment details and filter conditions.
* **`DELETE /{id}`**
  * **Purpose:** Soft or hard deletes an audience segment from the system.
* **`POST /{id}/evaluate`**
  * **Purpose:** Triggers an async background job that runs the segment's SQL filter against the customer database to compute the total number of matching customers.
* **`GET /{segmentId}/members`**
  * **Purpose:** Retrieves a paginated list of all customer records that belong to this segment (dynamically computed or statically assigned).

## 6. Campaigns (`/api/v1/campaigns`)
Marketing campaign orchestration.
* **`POST /`**
  * **Purpose:** Creates a new base campaign manually.
* **`GET /`**
  * **Purpose:** Retrieves a paginated list of all campaigns.
* **`GET /{id}`**
  * **Purpose:** Retrieves campaign details, including aggregated tracking metrics (total sent, clicked, converted).
* **`PATCH /{id}/status`**
  * **Purpose:** Updates the state machine of the campaign (e.g., pausing a running campaign, cancelling a scheduled one).
* **`GET /{id}/performance`**
  * **Purpose:** Retrieves live statistical performance, including delivery rates, open rates, CTRs, conversion rates, and revenue attributed to the campaign from the `v_campaign_performance` view.
* **`POST /{id}/execute`**
  * **Purpose:** **The Go-Live Trigger.** Executes a fully prepared campaign. It resolves the attached segment, fetches matching customer profiles, applies the message variants, and pushes the jobs to the asynchronous `EmailDispatchService` or WhatsApp API.
* **`POST /{id}/simulate`**
  * **Purpose:** Triggers a Monte Carlo simulation dry-run for a specific campaign to predict conversion outcomes before going live.
* **`GET /opt-out-alerts`**
  * **Purpose:** Fetches proactive alerts for all active campaigns whose opt-out rate is approaching or exceeding the safety threshold.
* **`GET /{id}/corrections`**
  * **Purpose:** Retrieves all self-correction events where the AI autonomously modified the campaign while running (e.g., switching channels due to poor performance).

## 7. Message Variants (`/api/v1/variants`)
A/B testing and message templates.
* **`POST /`**
  * **Purpose:** Creates a new message variant (e.g., HTML Email, WhatsApp Text) and attaches it to a parent campaign.
* **`GET /campaign/{campaignId}`**
  * **Purpose:** Lists all the specific variants attached to a campaign.
* **`GET /{campaignId}/mab-stats`**
  * **Purpose:** Retrieves Thompson Sampling Multi-Armed Bandit (MAB) statistics for all variants in the campaign, showing dynamic alpha/beta posterior values and 95% confidence intervals.
* **`GET /{id}`**
  * **Purpose:** Fetches the details, HTML copy, and conversion stats of a specific variant.
* **`PATCH /{id}`**
  * **Purpose:** Updates the content or configuration of an existing message variant.
* **`DELETE /{id}`**
  * **Purpose:** Deletes a specific message variant from its parent campaign.

## 8. Communications Log (`/api/v1/communications`)
Message delivery tracking.
* **`GET /campaign/{campaignId}`**
  * **Purpose:** Retrieves a log of all messages (sent, delivered, failed) associated with a specific campaign.
* **`GET /customer/{customerId}`**
  * **Purpose:** Retrieves a log of all messages ever sent to a specific customer profile.

## 9. AI Memory & Learning (`/api/v1/memory`)
* **`GET /`**
  * **Purpose:** Retrieves the Sovereign Agent's long-term memory records (e.g., "Email Subject X had a 40% higher open rate").
* **`GET /query`**
  * **Purpose:** Queries organizational memory explicitly filtered by `segmentTag` and `channel`. Returns past insights that apply to those criteria.

## 10. AI Corrections (`/api/v1/corrections`)
* **`GET /`**
  * **Purpose:** Retrieves a log of system-wide AI self-correction events.

## 11. AI Simulations (`/api/v1/simulations`)
* **`POST /`**
  * **Purpose:** Spawns a dry-run simulation of a campaign to predict CTRs and delivery volumes before live execution.
* **`GET /{id}`**
  * **Purpose:** Retrieves the results of a specific simulation run.

## 12. Audit Logs (`/api/v1/audit-logs`)
Security and tracking trails.
* **`GET /entity/{entityType}/{entityId}`**
  * **Purpose:** Retrieves audit trails related to changes made to a specific entity (e.g., seeing who edited a Segment).
* **`GET /trace/{traceId}`**
  * **Purpose:** Retrieves all system actions associated with a specific trace ID for distributed debugging.
* **`GET /actor/{actorId}`**
  * **Purpose:** Retrieves all actions taken by a specific human user or AI agent.

## 13. External Callbacks (`/api/v1/callbacks/channel`)
* **`POST /`**
  * **Purpose:** Webhook endpoint intended to be exposed to external providers (like SendGrid or WhatsApp Cloud API). Receives delivery receipts and read-receipts to update internal variant metrics.

## 14. Development Stub (`/api/v1/stub`)
* **`POST /send`**
  * **Purpose:** Internal development stub used for end-to-end testing without making costly real-world API requests to external providers.
