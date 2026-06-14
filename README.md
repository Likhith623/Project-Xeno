<div align="center">

# 🧬 Project Xeno

### The World's First Multi-Agent Autonomous CRM

**A next-generation Customer Relationship Management platform where an ensemble of AI agents autonomously creates, simulates, optimises, and executes marketing campaigns — with humans in the loop for final approval.**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Next.js](https://img.shields.io/badge/Next.js-16.2.9-black?logo=next.js)](https://nextjs.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue?logo=postgresql)](https://www.postgresql.org/)
[![Gemini AI](https://img.shields.io/badge/Gemini-2.5%20Flash-purple?logo=google)](https://deepmind.google/technologies/gemini/)
[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-blue?logo=typescript)](https://www.typescriptlang.org/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

---

*Built with Google Gemini 2.5 Flash · Thompson Sampling · ReAct Agents · Multi-Agent Debate · Bayesian Optimisation*

</div>

---

## 📋 Table of Contents

1. [Vision & Philosophy](#-vision--philosophy)
2. [Live Demo & Deployment](#-live-demo--deployment)
3. [Full System Architecture](#-full-system-architecture)
4. [Technology Stack](#-technology-stack)
5. [Backend Package Architecture](#-backend-package-architecture)
6. [Frontend Application Architecture](#-frontend-application-architecture)
7. [Database Schema](#-database-schema)
8. [AI Feature Deep-Dives](#-ai-feature-deep-dives) — All 24 Features
   - [Phase 0: Foundational CRM](#phase-0-foundational-crm-the-data-layer)
   - [Phase 1: Trust & Strategy Layer](#phase-1-trust--strategy-layer)
   - [Phase 2: Autonomous Execution Layer](#phase-2-the-autonomous-execution-layer)
   - [Phase 3: AGI Frontier](#phase-3-the-agi-frontier)
   - [Phase 4: Human Escalation](#phase-4-human-escalation--the-butler)
   - [Phase 5: Multi-Agent Architecture](#phase-5-multi-agent-architecture)
   - [Phase 6: Omni-Awareness](#phase-6-omni-awareness)
9. [Complete API Reference](#-complete-api-reference)
10. [End-to-End Data Flows](#-end-to-end-data-flows)
11. [Security Architecture](#-security-architecture)
12. [Scheduled Jobs Reference](#-scheduled-jobs-reference)
13. [Local Development Setup](#-local-development-setup)
14. [Environment Variables](#-environment-variables)
15. [Deployment](#-deployment)

---

## 🌟 Vision & Philosophy

Project Xeno is built on a single, radical premise: **the next-generation CRM should not be a passive database that marketers query — it should be an active, intelligent partner that thinks, acts, and learns alongside them.**

### The Core Problem

Modern CRM platforms are fundamentally broken for three reasons:

1. **They are reactive, not proactive.** You export a CSV, upload it to Mailchimp, and blast everyone. By the time you analyse the results, the market has moved.
2. **They waste money.** Brands blast SMS to users who would have converted via free email. They give 20% discounts to VIPs who would have paid full price.
3. **They forget everything.** When a senior marketer quits, the institutional knowledge they built over years — what subject lines work, which segments convert, when to use SMS vs email — disappears overnight.

### The Xeno Solution

Xeno eliminates all three problems simultaneously:

- **Proactive AI Agents** autonomously monitor your data 24/7, creating campaigns when opportunities arise (churn risk, dead inventory, weather events)
- **Autonomous Budget & Channel Optimization** routes every message to the most cost-effective channel per-user, in real-time
- **Organizational Memory** captures every campaign's lessons as structured knowledge, which future AI agents consult automatically

The result: **marketers become Editors, not Creators.** Xeno does the heavy lifting; humans approve the output.

---

## 🚀 Live Demo & Deployment

| Service | URL | Status |
|---|---|---|
| **Frontend** | `https://project-xeno-frontend.vercel.app` | Live |
| **Backend API** | `https://project-xeno.onrender.com/api/v1` | Live (cold-start ~30s) |
| **OpenAPI Spec** | `https://project-xeno.onrender.com/swagger-ui.html` | Live |
| **Health Check** | `https://project-xeno.onrender.com/actuator/health` | Live |

> **Note:** The backend runs on Render's free tier. The first request after inactivity may take ~30 seconds due to a cold start. This is a hosting constraint, not an application issue.

---

## 🏛️ Full System Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           PROJECT XENO — SYSTEM OVERVIEW                    │
└─────────────────────────────────────────────────────────────────────────────┘

  ┌─────────────────────────────────────────────────────┐
  │                   FRONTEND (Next.js 16)              │
  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌────────┐ │
  │  │Dashboard │ │Campaigns │ │ Segments │ │  MAB   │ │
  │  └──────────┘ └──────────┘ └──────────┘ └────────┘ │
  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌────────┐ │
  │  │Proposals │ │  Agent   │ │  Memory  │ │Orders  │ │
  │  └──────────┘ └──────────┘ └──────────┘ └────────┘ │
  │                                                      │
  │  React Query (Server State) + Zustand (Client State) │
  │  Axios → REST API → API Key Auth Header              │
  └───────────────────────┬─────────────────────────────┘
                          │ HTTPS / REST
                          ▼
  ┌────────────────────────────────────────────────────────────────────────────┐
  │                   BACKEND (Spring Boot 3.3 / Java 17)                      │
  │                                                                            │
  │  ┌─────────────────────────────────────────────────────────────────────┐  │
  │  │                     API GATEWAY LAYER                               │  │
  │  │  ApiKeyAuthenticationFilter → SecurityConfig → CORS → Controllers  │  │
  │  └─────────────────────────────────────────────────────────────────────┘  │
  │                                                                            │
  │  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────────┐    │
  │  │Customer │  │Campaign │  │Segment  │  │Variant  │  │   Agent     │    │
  │  │ Module  │  │ Module  │  │ Module  │  │ Module  │  │   Module    │    │
  │  └────┬────┘  └────┬────┘  └────┬────┘  └────┬────┘  └──────┬──────┘    │
  │       │            │            │            │              │            │
  │  ┌────▼────┐  ┌────▼────┐  ┌───▼─────┐  ┌───▼─────┐  ┌────▼───────┐    │
  │  │Order    │  │Simulator│  │Memory   │  │Comm.    │  │  Scheduler │    │
  │  │Product  │  │Module   │  │Module   │  │Module   │  │  (@Async)  │    │
  │  └────┬────┘  └────┬────┘  └───┬─────┘  └───┬─────┘  └────┬───────┘    │
  │       └────────────┴───────────┴─────────────┘             │            │
  │                         │                                   │            │
  │  ┌──────────────────────▼───────────────────────────────────▼────────┐  │
  │  │              INTELLIGENCE LAYER (AgentLlmGatewayService)           │  │
  │  │                  Google Gemini 2.5 Flash API                       │  │
  │  │   ReAct Prompting · Chain-of-Thought · Tool Use · Prompt Chaining  │  │
  │  └───────────────────────────────────────────────────────────────────┘  │
  │                                                                            │
  │  ┌─────────────────────────────────────────────────────────────────────┐  │
  │  │                      CHANNEL DISPATCH LAYER                         │  │
  │  │   SmartRoutingService → ChannelDispatchService → Email/SMS/WA/RCS  │  │
  │  └─────────────────────────────────────────────────────────────────────┘  │
  │                                                                            │
  │  SCHEDULED JOBS (Spring @Scheduled):                                       │
  │  VariantEvolutionJob │ CampaignFundManagerJob │ JourneyFallbackService     │
  │  PredictiveChurnJob  │ ChannelFatigueJob      │ VipConciergeEscalationJob  │
  │  ExternalTriggerJob  │ PredictiveInventoryJob │ ZeroPartyLookalikeJob      │
  │  MicroChurnWhispererJob                                                    │
  └──────────────────────────────────────┬─────────────────────────────────────┘
                                         │ JDBC / JPA
                                         ▼
  ┌──────────────────────────────────────────────────────────────────────────┐
  │                    PostgreSQL 15+ (Supabase / Railway)                   │
  │                                                                          │
  │  customers │ customer_metrics │ orders │ order_items │ products          │
  │  audience_segments │ segment_members │ campaigns │ message_variants      │
  │  communications │ memory │ agent_sessions │ agent_decisions              │
  │  audit_logs │ corrections │ simulation_runs │ simulation_results         │
  └──────────────────────────────────────────────────────────────────────────┘
```

### Data Flow: A Campaign Being Sent

```
Marketer clicks "Approve"
        │
        ▼
POST /campaigns/{id}/approve
        │
        ▼
CampaignService.approveCampaign()
  → sets status = RUNNING
  → calls CampaignExecutionService.executeCampaignAsync() [@Async]
        │
        ▼ (Background Thread)
CampaignExecutionService
  │
  ├─→ 1. MultiArmedBanditService.selectBestVariant()
  │       Uses Apache Commons Math BetaDistribution
  │       Thompson Sampling: sample α/(α+β) per variant, pick highest
  │
  ├─→ 2. SegmentQueryBuilder.buildQuery(filterJson)
  │       Compiles safe parameterized SQL from JSON rules
  │       JdbcTemplate.queryForList(sql, params)
  │
  └─→ 3. For each target customer:
          ├─→ SmartRoutingService.resolveOptimalChannel()
          │     Checks CustomerMetrics.emailOpenRate, smsOpenRate
          │     Overrides channel if Email > 70% open rate
          │
          ├─→ HyperPersonalizationService.personalise()
          │     Injects customer.traits JSON into LLM prompt
          │     Returns individually rewritten message
          │
          ├─→ ChannelFatigueCooldown check
          │     If channelCooldownUntil > NOW() → SKIP
          │
          └─→ ChannelDispatchService.send()
                Creates CommunicationEntity (status=SENT)
                Updates campaign.sentCount
```

---

## 🛠️ Technology Stack

### Backend

| Component | Technology | Purpose |
|---|---|---|
| **Runtime** | Java 17 | Core language |
| **Framework** | Spring Boot 3.3.0 | Web, DI, Security, Scheduling |
| **ORM** | Spring Data JPA + Hibernate | Database access |
| **Database** | PostgreSQL 15+ | Primary data store |
| **Migrations** | Flyway | Schema versioning |
| **AI** | Google Gemini 2.5 Flash | LLM for all AI features |
| **Math** | Apache Commons Math 3 | Thompson Sampling Beta Distribution |
| **Mapping** | MapStruct 1.5.5 | Entity ↔ DTO transformation |
| **Boilerplate** | Lombok 1.18 | Annotations (Builder, Slf4j, etc.) |
| **Security** | Spring Security + API Key | Request authentication |
| **Async** | Spring @Async + TaskExecutor | Non-blocking campaign execution |
| **JSON** | Jackson ObjectMapper | LLM response parsing |
| **Scheduler** | Spring @Scheduled | Cron and fixed-rate jobs |
| **Validation** | Spring Validation | Request DTO validation |
| **Observability** | Spring Actuator | Health, metrics endpoints |

### Frontend

| Component | Technology | Purpose |
|---|---|---|
| **Framework** | Next.js 16.2.9 | App Router, SSR/SSG |
| **Language** | TypeScript 5 | Type safety |
| **Styling** | Tailwind CSS 4 | Utility-first CSS |
| **UI Components** | shadcn/ui + @base-ui/react | Primitive UI components |
| **Server State** | TanStack React Query 5 | Data fetching, caching, invalidation |
| **Client State** | Zustand 5 (with persist) | Global state + localStorage persistence |
| **Charts** | Recharts 3.8 | Campaign analytics visualisations |
| **Icons** | Lucide React | Consistent icon set |
| **HTTP Client** | Axios 1.17 | API calls with interceptors |
| **Notifications** | Sonner 2 | Toast notifications |
| **Animations** | tw-animate-css | CSS animation utilities |

### Infrastructure

| Service | Platform | Notes |
|---|---|---|
| **Backend** | Render.com | Dockerised, free tier |
| **Frontend** | Vercel | Next.js native deployment |
| **Database** | Supabase / Railway PostgreSQL | Managed PostgreSQL |
| **Containerisation** | Docker + Dockerfile | Multi-stage build |

---

## 📦 Backend Package Architecture

```
com.xenocrm/
│
├── XenoCrmApplication.java          # Spring Boot entry point
│
├── agent/                           # Sovereign AI Agent
│   ├── controller/
│   │   └── AgentController.java     # POST /agent/chat, GET /sessions/{id}
│   ├── dto/
│   │   ├── AgentChatRequestDto.java
│   │   ├── AgentChatResponseDto.java
│   │   ├── AgentDecisionResponseDto.java
│   │   └── AgentSessionResponseDto.java
│   ├── entity/
│   │   ├── AgentSessionEntity.java  # Persists session state + plan
│   │   └── AgentDecisionEntity.java # Each ReAct step (Thought/Action/Observation)
│   ├── enums/
│   │   ├── AgentDecisionType.java   # MEMORY_LOOKUP, SEGMENT_SEARCH, etc.
│   │   └── AgentSessionStatus.java  # RUNNING, COMPLETED, FAILED
│   ├── repository/
│   │   ├── AgentSessionRepository.java
│   │   └── AgentDecisionRepository.java
│   └── service/
│       ├── AgentOrchestrationService.java  # Core ReAct loop
│       ├── AgentLlmGatewayService.java     # Gemini API client
│       └── tools/
│           ├── AgentTool.java              # Tool interface
│           ├── FetchCustomerMetricsTool.java
│           └── GenerateCampaignTool.java
│
├── campaign/                        # Campaign Management + AI Jobs
│   ├── controller/
│   │   └── CampaignController.java  # CRUD + /approve + /proposals + /timeline
│   ├── entity/
│   │   └── CampaignEntity.java      # name, goal, status, budgetAllocated, ROAS tracking
│   ├── enums/
│   │   └── CampaignStatus.java      # DRAFT, SCHEDULED, RUNNING, COMPLETED, CANCELLED
│   └── service/
│       ├── CampaignService.java           # CRUD orchestration
│       ├── CampaignExecutionService.java  # Async dispatch pipeline
│       ├── CampaignAnalyticsService.java  # Natural language analytics + LLM
│       ├── TimelineStorytellingService.java
│       │
│       ├── ── AUTONOMOUS JOBS ──
│       ├── CampaignFundManagerJob.java    # @Scheduled hourly ROAS rebalancing
│       ├── ChannelFatigueJob.java         # @Scheduled 14-day cooldown engine
│       ├── DynamicDiscountService.java    # Per-user discount calculation
│       ├── ExternalTriggerJob.java        # @Scheduled weather/event triggers
│       ├── HyperPersonalizationService.java  # Per-user LLM content rewriting
│       ├── JourneyFallbackService.java    # @Scheduled email→SMS fallback
│       ├── MicroChurnWhispererJob.java    # @Scheduled subtle churn prevention
│       ├── MultiAgentDebateService.java   # 3-agent war room prompt chain
│       ├── PredictiveChurnJob.java        # @Scheduled churn risk detection
│       ├── PredictiveInventoryJob.java    # @Scheduled dead stock clearance
│       ├── VipConciergeEscalationJob.java # @Scheduled Slack whale alerts
│       └── ZeroPartyLookalikeJob.java     # @Scheduled AI SQL lookalike synthesis
│
├── channelservice/                  # Multi-Channel Dispatch
│   ├── service/
│   │   ├── ChannelDispatchService.java    # Routes to correct channel
│   │   ├── SmartRoutingService.java       # Per-user channel optimisation
│   │   └── SlackNotificationService.java  # VIP escalation Slack webhooks
│   └── enums/
│       └── MessageChannel.java            # email, sms, whatsapp, rcs
│
├── customer/                        # Customer 360
│   ├── entity/
│   │   ├── CustomerEntity.java      # name, email, phone, globallyOptedOut, traits JSONB
│   │   └── CustomerMetricsEntity.java  # ltv, recencyDays, frequency, emailOpenRate
│   └── service/
│       └── CustomerMetricsService.java   # Real-time RFM recalculation on order
│
├── segment/                         # Dynamic Audience Segmentation
│   ├── entity/
│   │   └── AudienceSegmentEntity.java   # name, filterJson, filterSql, type
│   ├── service/
│   │   ├── SegmentEvaluationService.java  # @Async SQL evaluation
│   │   └── PersonaGenerationService.java  # LLM buyer persona from segment SQL
│   └── util/
│       └── SegmentQueryBuilder.java       # Safe SQL parameterization
│
├── variant/                         # A/B Variants + MAB
│   ├── entity/
│   │   └── MessageVariantEntity.java    # subjectLine, bodyHtml, mabAlpha, mabBeta
│   └── service/
│       ├── MultiArmedBanditService.java  # Thompson Sampling with BetaDistribution
│       └── VariantEvolutionJob.java      # @Scheduled LLM-powered variant breeding
│
├── simulator/                       # Counterfactual Simulation
│   └── service/
│       ├── CounterfactualSimulationService.java  # LLM-based what-if prediction
│       ├── MonteCarloSimulationEngine.java
│       └── AudienceSimulationOrchestrationService.java
│
├── memory/                          # Organizational Memory
│   └── service/
│       └── MemoryRetrievalService.java  # Post-campaign insight extraction + storage
│
├── communication/                   # Message Records
│   └── entity/
│       └── CommunicationEntity.java   # Every sent/delivered/opened/bounced event
│
├── audit/                           # Audit Logging
│   └── entity/
│       └── AuditLogEntity.java
│
├── correction/                      # Human Feedback Loop
│
├── security/
│   └── ApiKeyAuthenticationFilter.java  # X-API-KEY header validation
│
└── test/
    └── controller/
        └── AITestingController.java  # /trigger-war-room, /trigger-fund-manager, etc.
```

---

## 🖥️ Frontend Application Architecture

```
frontend/src/
│
├── app/                             # Next.js App Router
│   ├── page.tsx                     # Dashboard — KPI cards, charts, active campaigns
│   ├── campaigns/
│   │   ├── page.tsx                 # Campaign list + create modal
│   │   └── [id]/page.tsx            # Campaign detail — variants, simulate, timeline
│   ├── proposals/
│   │   └── page.tsx                 # Tinder swipe UI — AI proposals inbox
│   ├── agent/
│   │   └── page.tsx                 # Sovereign AI Agent chat + reasoning trace
│   ├── segments/
│   │   └── page.tsx                 # Segment management + AI War Room + Persona Gen
│   ├── customers/
│   │   └── page.tsx                 # Customer table + Create/Edit/Email modals
│   ├── orders/
│   │   └── page.tsx                 # Order table + Create (customer picker) + status
│   ├── products/
│   │   └── page.tsx                 # Product catalog + filter/search + Add/Edit modals
│   ├── mab/
│   │   └── page.tsx                 # MAB Dashboard — Thompson Sampling live stats
│   ├── memory/
│   │   └── page.tsx                 # Org Memory viewer + AI memory query + agent chat
│   ├── audit-logs/
│   │   └── page.tsx                 # Audit trail log viewer
│   └── corrections/
│       └── page.tsx                 # Human feedback corrections
│
├── components/
│   ├── layout/
│   │   ├── Shell.tsx                # Page wrapper with Sidebar + Topbar
│   │   ├── Sidebar.tsx              # Navigation + active campaign count
│   │   └── Topbar.tsx               # Page title + contextual actions
│   └── ui/                          # shadcn/ui primitives
│       ├── button.tsx, card.tsx, badge.tsx, input.tsx
│       ├── dialog.tsx               # Custom dialog (fixed for @base-ui)
│       └── avatar.tsx, dropdown-menu.tsx, select.tsx ...
│
├── lib/
│   ├── api.ts                       # Axios instance + envelope unwrapper
│   └── utils.ts                     # cn() classname helper
│
└── store/
    ├── useAgentStore.ts             # Zustand (persisted) — sessionId, messages, lastCampaignId
    ├── useCampaignStore.ts          # Zustand — modal open/close state
    └── store.ts                     # Zustand — sidebar state
```

### State Management Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    STATE MANAGEMENT LAYERS                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  SERVER STATE (TanStack React Query)                             │
│  ─────────────────────────────────────────────────────────────  │
│  queryKey: ['campaigns']           staleTime: 30,000ms           │
│  queryKey: ['campaigns', 'proposals']                            │
│  queryKey: ['agent-session', sessionId]  refetchInterval: 3000ms │
│  queryKey: ['mab-stats', campaignId]     (until COMPLETED)       │
│  queryKey: ['memory', filter, channel]                           │
│                                                                  │
│  CLIENT STATE (Zustand + localStorage persist)                   │
│  ─────────────────────────────────────────────────────────────  │
│  useAgentStore: sessionId, messages[], lastCreatedCampaignId     │
│  useCampaignStore: isCampaignModalOpen                           │
│  useAppStore: isAgentSidebarOpen                                 │
│                                                                  │
│  API RESPONSE ENVELOPE UNWRAPPING (api.ts)                       │
│  ─────────────────────────────────────────────────────────────  │
│  { success: true, data: <payload>, pagination: {...} }           │
│  → unwrap() → returns <payload> with _pagination attached        │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Schema

The database is managed via **Flyway migrations** applied on startup. Key tables:

```sql
-- Core CRM
customers            (id UUID PK, name, email, phone, globallyOptedOut, customAttributes JSONB)
customer_metrics     (id UUID PK, customerId FK, monetaryTotal, recencyDays, frequency,
                      avgOrderValue, emailOpenRate, smsOpenRate, churnRisk, channelCooldownUntil)
orders               (id UUID PK, customerId FK, totalAmount, status, orderNumber)
order_items          (id UUID PK, orderId FK, productId FK, quantity, price)
products             (id UUID PK, name, sku, price, brand, categoryId FK, active)
product_categories   (id UUID PK, name, description)

-- Segmentation
audience_segments    (id UUID PK, name, filterSql, filterJson JSONB, type, status, customerCount)
segment_members      (segmentId FK, customerId FK, addedAt)

-- Campaigns
campaigns            (id UUID PK, name, goal, status, targetSegmentId FK, channel,
                      budgetAllocated, currentSpend, revenueAttributed, createdByAgent,
                      startedAt, completedAt, sentCount, openCount, clickCount)
message_variants     (id UUID PK, campaignId FK, subjectLine, bodyHtml, channel,
                      mabAlpha DECIMAL, mabBeta DECIMAL, mabImpressions, mabConversions,
                      mabIsActive)

-- Communications
communications       (id UUID PK, campaignId FK, variantId FK, customerId FK, channel,
                      status, recipientAddress, personalisedSubject, personalisedBody,
                      sentAt, openedAt, clickedAt, bouncedAt)

-- AI Agent
agent_sessions       (id UUID PK, goal TEXT, status, modelUsed, plan JSONB,
                      conversationLog JSONB, tokensUsedIn, tokensUsedOut, startedAt, endedAt)
agent_decisions      (id UUID PK, sessionId FK, decisionType, thought TEXT,
                      action TEXT, observation TEXT, reasoning TEXT, createdAt)

-- Intelligence
memory               (id UUID PK, learningSummary TEXT, learningType, channel,
                      segmentTag, confidence, avgLift, evidenceCount)
audit_logs           (id UUID PK, entityType, entityId, action, oldValue JSONB,
                      newValue JSONB, performedBy, createdAt)

-- Simulation
simulation_runs      (id UUID PK, campaignId FK, status, channel, startedAt, completedAt)
simulation_results   (id UUID PK, runId FK, predictedOpenRate, predictedCtr,
                      predictedConversionRate, predictedRevenue, confidenceIntervalLow,
                      confidenceIntervalHigh, reasoning TEXT)
```

---

## 🤖 AI Feature Deep-Dives

### Phase 0: Foundational CRM — The Data Layer

These features form the bedrock. Without accurate, real-time data, AI is useless.

---

#### Feature 1 — Customer 360 & Dynamic Ingestion Engine

**What it does:** Unifies customer data from any source into a single, canonical identity. Normalises emails, merges JSON trait objects without overwriting existing fields, and tracks global opt-out status.

**Files:** `CustomerController.java`, `CustomerService.java`, `CustomerEntity.java`

**Key Technical Detail:** The `customAttributes` column is a PostgreSQL `JSONB` field, allowing indexing of arbitrary nested properties. A `PATCH` request deep-merges the JSON rather than replacing it.

**API:**
```http
POST /api/v1/customers
{
  "name": "Sarah Jenkins",
  "email": "sarah@example.com",
  "traits": { "loyalty_tier": "Platinum", "shoe_size": "8" }
}

PATCH /api/v1/customers/{id}
{ "traits": { "preferred_color": "red" } }
→ Deep-merged; loyalty_tier preserved
```

**End-to-End Flow:** Shopify webhook → `POST /customers` → `CustomerService.createOrUpdate()` → PostgreSQL UPSERT on email → Instant segment re-eligibility.

---

#### Feature 2 — Real-Time RFM Computation Engine

**What it does:** The moment an order is recorded, the system synchronously recalculates the customer's Recency (days since last order), Frequency (total orders), and Monetary (lifetime value) scores. Stale segments are impossible.

**Files:** `OrderController.java`, `CustomerMetricsService.java`, `CustomerMetricsEntity.java`

**Key Technical Detail:** Executed inside an ACID-compliant PostgreSQL transaction. `recencyDays` is set to `0` instantly on purchase, causing the customer to drop out of any "At Risk" segment on the next evaluation.

**API:**
```http
POST /api/v1/orders
{
  "customerId": "123e4567-e89b-12d3-a456-426614174000",
  "totalAmount": 450.00,
  "status": "PENDING"
}
→ CustomerMetrics updated synchronously
→ Segment re-evaluation triggered asynchronously
```

---

#### Feature 3 — Real-Time Dynamic SQL Segmentation

**What it does:** Segments are living SQL queries, not static lists. Every time a campaign executes, the segment is re-evaluated against the current state of the database. The `SegmentQueryBuilder` safely parameterizes all queries to prevent SQL injection.

**Files:** `SegmentController.java`, `SegmentEvaluationService.java`, `SegmentQueryBuilder.java`

**Key Technical Detail:** `SegmentQueryBuilder.buildQuery(filterJson)` compiles a JSON rule object like `{"field": "ltv", "op": ">", "value": 1000}` into a fully parameterized `PreparedStatement`-compatible query. The execution layer uses `JdbcTemplate.queryForList(sql, params)`.

**API:**
```http
POST /api/v1/segments
{
  "name": "Whales At Risk",
  "filterSql": "ltv > 1000 AND days_since_last_order > 90",
  "type": "DYNAMIC"
}

POST /api/v1/segments/{id}/evaluate
→ Re-counts segment and updates customerCount asynchronously

GET /api/v1/segments/{id}/members
→ Paginated list of current segment members
```

---

### Phase 1: Trust & Strategy Layer

*The AI proves its value before earning human trust.*

---

#### Feature 4 — AI Explainability Layer (The Audit Trail)

**What it does:** Every action the Sovereign AI Agent takes — every "Thought", every database query, every decision — is permanently recorded in the `agent_decisions` table and exposed via API. The AI is never a black box.

**Files:** `AgentOrchestrationService.java`, `AgentDecisionEntity.java`, `AgentController.java`

**Architecture:** The agent uses the **ReAct** (Reasoning + Acting) framework. After each LLM call, the response is parsed for `Thought:`, `Action:`, and `Observation:` components. These are persisted as individual `AgentDecisionEntity` records, forming a chronological audit trail.

```
User Prompt → AgentOrchestrationService
  │
  ├─ Step 1: LLM Call (reason)
  │     Thought: "I need to find the VIP segment"
  │     Action: search_segments("VIP")
  │     → save AgentDecisionEntity (MEMORY_LOOKUP)
  │
  ├─ Step 2: Execute Action
  │     Query segments table
  │     Observation: "Found segment 'VIP' with ID: abc-123"
  │     → save AgentDecisionEntity
  │
  └─ Step 3: LLM Call (act)
        Generate campaign draft
        → save AgentDecisionEntity (GENERATE_CAMPAIGN)
        → save CampaignEntity (status=DRAFT)
```

**API:**
```http
GET /api/v1/agent/sessions/{sessionId}/decisions
Response:
[
  {
    "decisionType": "MEMORY_LOOKUP",
    "thought": "I need to find what works for VIPs",
    "reasoning": "Queried memory for VIP segment insights"
  },
  {
    "decisionType": "GENERATE_CAMPAIGN",
    "thought": "Draft a win-back campaign for the VIP segment",
    "reasoning": "Target segment identified. Creating campaign draft."
  }
]
```

---

#### Feature 5 — Counterfactual Campaign Simulator

**What it does:** Before spending a single dollar, marketers can simulate the outcome of their campaign. The system feeds campaign context to Gemini and asks it to predict open rates, CTR, conversion rates, and revenue with confidence intervals.

**Files:** `CounterfactualSimulationService.java`, `SimulationController.java`, `MonteCarloSimulationEngine.java`

**Architecture:**
```
POST /simulations/campaigns/{id}/counterfactual?channel=EMAIL
  │
  ├─ Fetch CampaignEntity (goal, segment, variants)
  ├─ Construct LLM prompt:
  │   "You are a Campaign Simulator. Goal: win-back VIPs.
  │    What are predicted results if sent via EMAIL?
  │    Return JSON: {predictedOpenRate, predictedCtr,
  │                 predictedRevenue, confidenceInterval}"
  ├─ callGemini(prompt) → strip markdown fences → parse JSON
  └─ Return CounterfactualResultDto
```

**API:**
```http
GET /api/v1/simulations/campaigns/{id}/counterfactual?channel=EMAIL
Response:
{
  "channel": "EMAIL",
  "predictedOpenRate": 0.45,
  "predictedCtr": 0.05,
  "predictedConversionRate": 0.02,
  "predictedRevenue": 37000.00,
  "reasoning": "Email has lower CTR but significantly higher reach for this segment type."
}
```

---

#### Feature 6 — Natural Language Analytics

**What it does:** Converts raw campaign numbers (opens, clicks, bounces, revenue) into a plain-English, executive-ready narrative paragraph. The CMO gets a 3-sentence summary instead of a dashboard of numbers.

**Files:** `CampaignAnalyticsService.java`

**Architecture:** Queries `campaigns` + `communications` tables, calculates all metrics, then constructs a detailed prompt: "You are a Senior Data Analyst. Here are the raw stats: {...}. Write a 3-sentence narrative for a non-technical executive."

**API:**
```http
GET /api/v1/campaigns/{id}/analytics/narrative
Response:
{
  "narrative": "The 'Summer Blowout' campaign drove $14,200 from 3,450 customers
  with a 22% open rate, significantly above the 18% baseline. However, the SMS
  channel showed an abnormal 8% unsubscribe spike, suggesting fatigue in that
  cohort. We recommend pausing SMS for this segment for 14 days."
}
```

---

#### Feature 7 — Organizational Memory Retrieval

**What it does:** After every campaign, the AI extracts the core lesson ("SMS doesn't work for Gen Z") and saves it to the `memory` table. All future agent sessions automatically retrieve relevant memories and inject them into their system prompt, creating a **compounding institutional knowledge base**.

**Files:** `MemoryRetrievalService.java`, `MemoryController.java`

**Architecture:**
```
Campaign COMPLETED
  │
  └─ MemoryRetrievalService.extractAndSave()
       LLM Prompt: "Campaign finished. Stats: {...}. What is the key lesson?"
       → save MemoryEntity (learningSummary, channel, segmentTag, confidence, avgLift)

Future Agent Session
  │
  └─ AgentOrchestrationService.runAsyncOrchestration()
       GET /memory?segmentTag=VIP&channel=email
       → inject memories into LLM system prompt
       → agent avoids past mistakes automatically
```

**API:**
```http
GET /api/v1/memory
GET /api/v1/memory/query?segmentTag=VIP&channel=email
GET /api/v1/memory/ask?query=what works for high LTV users
Response: [
  {
    "learningSummary": "SMS drives 4x higher conversion than email for Gen Z segments",
    "confidence": 0.87,
    "avgLift": 0.12,
    "evidenceCount": 7
  }
]
```

---

### Phase 2: The Autonomous Execution Layer

*The AI moves from advising to doing.*

---

#### Feature 8 — AI Persona Generator

**What it does:** Takes the cold SQL logic of a segment and generates a fully humanized "Buyer Persona" — a biography of the typical user. Copywriters get a person to write to, not a spreadsheet.

**Files:** `PersonaGenerationService.java`

**Architecture:**
```
GET /segments/{id}/persona
  │
  ├─ Fetch AudienceSegmentEntity (filterSql, statistical averages)
  ├─ LLM Prompt: "Segment SQL: ltv > 1000 AND gender = 'F'.
  │   Average age: 38. Top product: Premium Handbags.
  │   Create a buyer persona JSON:
  │   {name, ageRange, bio, motivations, recommendedTone}"
  └─ Parse JSON → return PersonaResponseDto
```

**API:**
```http
GET /api/v1/segments/{id}/persona
Response:
{
  "name": "High-Value Suburban Moms",
  "ageRange": "35-45",
  "bio": "Sarah is a busy professional mom who values time above all else...",
  "motivations": ["Convenience", "Quality over price", "Fast shipping"],
  "recommendedTone": "Empathetic, premium, direct"
}
```

---

#### Feature 9 — Campaign Timeline Storytelling

**What it does:** Every campaign event (Created, Dispatched, Delivered, Opened, Bounced) is aggregated from the `audit_logs` and `communications` tables into a chronological, human-readable timeline. No SQL skills needed to debug a campaign.

**Files:** `TimelineStorytellingService.java`

**API:**
```http
GET /api/v1/campaigns/{id}/timeline
Response:
[
  { "time": "09:00", "event": "Campaign approved by Admin" },
  { "time": "09:01", "event": "Segment evaluated: 4,500 users identified" },
  { "time": "09:05", "event": "Thompson Sampling selected Variant B as winner" },
  { "time": "09:06", "event": "4,490 emails sent. 10 bounced (invalid addresses)." }
]
```

---

#### Feature 10 — Automated A/B/n Content Evolution (Thompson Sampling + LLM Breeding)

**What it does:** The most advanced A/B testing system ever built for a CRM. It combines Bayesian mathematics (Thompson Sampling) with LLM content generation to automatically identify winning variants, discard losers, and **breed** new mutations of the winner — creating a self-improving campaign.

**Files:** `VariantEvolutionJob.java`, `MultiArmedBanditService.java`

**Architecture:**
```
VariantEvolutionJob (@Scheduled every 1 hour)
  │
  ├─ For each RUNNING campaign:
  │   ├─ Fetch all active MessageVariantEntity
  │   ├─ Check Thompson Sampling winner:
  │   │   if mabConversions > 50 AND mabAlpha > mabBeta * 1.5 → WINNER
  │   │
  │   └─ If winner found AND variants.size() < 5:
  │       LLM Prompt: "Variant A won: Subject='Exclusive 10% Offer'.
  │                   Create 2 mutations. Return JSON array of variants."
  │       → Parse → Save 2 new MessageVariantEntity (mabIsActive=true)

MultiArmedBanditService (called on every dispatch)
  │
  ├─ For each variant: sample BetaDistribution(mabAlpha, mabBeta)
  ├─ Pick variant with highest sample value
  └─ Return winner → CampaignExecutionService uses it for this batch
```

**Thompson Sampling Math:**
- Each variant has `α` (alpha = conversions + 1) and `β` (beta = failures + 1)
- Sample from `Beta(α, β)` distribution using Apache Commons Math
- Higher samples → more likely to be selected → natural exploration/exploitation balance
- As data accumulates, the winner's distribution narrows and dominates automatically

**API:**
```http
GET /api/v1/variants/campaign/{campaignId}          # All variants
GET /api/v1/campaigns/{campaignId}/variants/mab-stats  # Alpha, beta, conv. rates
```

---

#### Feature 11 — Budget & ROI Optimisation Agent (Smart Channel Routing)

**What it does:** Intercepts every outgoing message milliseconds before dispatch. If a user has historically opened 90% of emails, Xeno overrides the marketer's SMS choice and sends email instead — saving $0.05 per message while achieving the same result.

**Files:** `SmartRoutingService.java` (called from `CampaignExecutionService`)

**Architecture:**
```
CampaignExecutionService.dispatchToCustomer(customer, campaign)
  │
  └─ SmartRoutingService.resolveOptimalChannel(customer, requestedChannel)
       │
       ├─ Fetch CustomerMetrics
       ├─ If emailOpenRate > 0.70 → use EMAIL (free)
       ├─ If customer is VIP (ltv > 5000) && channel == EMAIL → upgrade to SMS
       ├─ If channelCooldownUntil > NOW() → SKIP entirely
       └─ Return resolvedChannel → ChannelDispatchService.send()
```

---

#### Feature 12 — Hyper-Personalisation (Segment of One)

**What it does:** At send-time, the base email template is rewritten by the LLM for each individual recipient based on their JSON traits. Not "Hello {{name}}" — the entire message structure changes.

**Files:** `HyperPersonalizationService.java`

**Architecture:**
```
HyperPersonalizationService.personalise(template, customer)
  │
  ├─ Build prompt:
  │   "Rewrite this email for a specific user.
  │    Base email: 'Check out our winter coats.'
  │    User traits: {city: Miami, color: vibrant, tier: Standard}
  │    Write ONLY the rewritten body. Max 100 words."
  │
  └─ Return personalised body → saved as communication.personalisedBody
```

**Example:**
- Base: *"Check out our winter coats."*
- Miami user: *"Skip the heavy layers! Our windbreakers are perfect for Miami nights."*
- Alaska VIP: *"Sarah, VIP early access: our warmest thermal parkas for Alaskan winters."*

---

#### Feature 13 — AI Multi-Channel Journey Builder

**What it does:** Autonomously follows up with users who ignored an email. 24 hours after a delivered-but-unopened email, the AI drafts a punchy SMS follow-up and sends it automatically. Zero marketer intervention.

**Files:** `JourneyFallbackService.java` (`@Scheduled fixedRate=300000`)

**Architecture:**
```
JourneyFallbackService (@Scheduled every 5 minutes)
  │
  ├─ Query communications:
  │   WHERE channel=EMAIL AND status=DELIVERED
  │   AND createdAt < NOW() - 24 HOURS
  │   LIMIT 1
  │
  ├─ For each unengaged communication:
  │   LLM: "The user ignored this email: '{body}'.
  │          Write a punchy SMS follow-up (max 160 chars)."
  │
  └─ Create new CommunicationEntity (channel=SMS)
     → ChannelDispatchService.send()
```

---

#### Feature 14 — Predictive Churn Interception

**What it does:** Monitors purchase velocity. When a user's buying cadence drops below their historical average, their churn risk score spikes. Xeno autonomously creates a "We Miss You" campaign draft and sends it to the proposals inbox.

**Files:** `PredictiveChurnJob.java` (`@Scheduled`)

**Architecture:**
```
PredictiveChurnJob (@Scheduled)
  │
  ├─ Query CustomerMetrics WHERE churnRisk > 0.7
  ├─ Create temporary segment: "Auto-Churn-Risk-{date}"
  ├─ LLM: "Draft a win-back campaign for users who haven't
  │          bought in X days. Offer: 15% off. Subject line + body."
  └─ Save CampaignEntity (status=DRAFT, createdByAgent=true)
     → Appears in /proposals inbox for human approval
```

---

#### Feature 15 — External Trigger Campaigns

**What it does:** Listens to external signals (weather, inventory levels, world events) and autonomously creates contextual campaigns. A snowstorm triggers a delivery campaign; hot weather triggers a sunscreen campaign.

**Files:** `ExternalTriggerJob.java` (`@Scheduled`)

**Architecture:**
```
ExternalTriggerJob (@Scheduled)
  │
  ├─ Simulate external API check (weather, stock price, etc.)
  ├─ Detect trigger condition met
  ├─ Identify affected customer segment (by location trait)
  ├─ LLM: "Create a contextually relevant campaign for users
  │          in New York during a snowstorm."
  └─ Save CampaignEntity (status=DRAFT)
     → Appears in /proposals inbox
```

---

#### Feature 16 — Human-in-the-Loop Swipe UI (The Proposals Inbox)

**What it does:** The ultimate safety mechanism. ALL AI-generated campaigns are staged in a Tinder-style "Proposals Inbox" where a human marketer reviews them with a simple Approve or Reject action. Nothing autonomous reaches customers without human sign-off.

**Files:** `proposals/page.tsx`, `CampaignController.java`

**Architecture:**
```
Frontend: proposals/page.tsx
  │
  ├─ GET /campaigns/proposals → all DRAFT campaigns with createdByAgent=true
  ├─ Displays: campaign name, goal, segment target, AI reasoning
  ├─ Stats panel: real approved/rejected counts + revenue from AI campaigns
  ├─ Approve → POST /campaigns/{id}/approve
  │   → status = RUNNING → executeCampaignAsync()
  │   → optimistic removal from inbox + toast with "View Campaign" link
  └─ Reject → PATCH /campaigns/{id}/status { status: CANCELLED }
      → optimistic removal from inbox
```

**UI Features:**
- Tinder-style card stack with Previous/Next navigation
- Real-time stats: "3 approved this month | 1 rejected | $14.2K revenue from AI"
- "View Last Approved" button for instant navigation
- All actioned proposals disappear immediately (optimistic UI)

---

### Phase 3: The AGI Frontier

*Capabilities that blur the line between software and intelligence.*

---

#### Feature 17 — Predictive Inventory Clearance

**What it does:** Bridges supply chain and marketing. Scans the product catalog for aging inventory (high stock count, added > 90 days ago) and creates targeted clearance campaigns aimed only at customers who have historically bought that product type.

**Files:** `PredictiveInventoryJob.java`

**Architecture:**
```
PredictiveInventoryJob (@Scheduled)
  │
  ├─ SELECT * FROM products WHERE inventory_count > 100
  │   AND days_since_added > 90
  ├─ For each dead stock item:
  │   LLM: "Product: 'XL Yellow T-Shirt'. Price: $29.
  │          Which customer segment is most likely to buy this?
  │          Write a clearance campaign."
  └─ Create segment + campaign → proposals inbox
```

---

#### Feature 18 — Dynamic Pricing Tiers

**What it does:** Calculates the mathematically optimal discount for each individual user during dispatch. VIPs who will buy anyway get 5%. Users on the verge of churning forever get 25%. Never give away a dollar of margin unnecessarily.

**Files:** `DynamicDiscountService.java`

**Architecture:**
```
DynamicDiscountService.calculateDiscount(customer)
  │
  ├─ IF churnRisk > 0.8 AND ltv > 2000 → 25% "Hail Mary" discount
  ├─ IF churnRisk < 0.2 AND ltv > 5000 → 5% "Appreciation" discount
  ├─ IF newCustomer (recencyDays < 7) → 10% "Welcome" discount
  └─ Generate unique discount code → inject into personalised body
```

---

#### Feature 19 — Zero-Party Lookalike Synthesis

**What it does:** Analyses the JSON traits and purchase patterns of your top 100 LTV customers. Asks the LLM to identify hidden mathematical patterns connecting them, then synthesises a PostgreSQL query to find "future whales" hidden in your database.

**Files:** `ZeroPartyLookalikeJob.java`

**Architecture:**
```
ZeroPartyLookalikeJob (@Scheduled)
  │
  ├─ SELECT top 100 customers by monetaryTotal
  ├─ Collect their customAttributes JSONB + purchase history
  ├─ LLM: "These are our top 100 customers. Their traits: {JSON}.
  │          Identify the hidden pattern. Write a PostgreSQL WHERE clause
  │          to find users who match this pattern but haven't bought yet."
  ├─ LLM returns SQL fragment
  └─ Save as new AudienceSegmentEntity: "Zero-Party Lookalike - {date}"
```

---

### Phase 4: Human Escalation — The Butler

---

#### Feature 20 — VIP Concierge Escalation

**What it does:** Detects when your most valuable customers (LTV > $5,000) are going silent (no order in 180+ days). Fires a Slack alert to the sales director with the customer's name, LTV, and an AI-written, empathetic phone script.

**Files:** `VipConciergeEscalationJob.java`, `SlackNotificationService.java`

**Architecture:**
```
VipConciergeEscalationJob (@Scheduled cron="0 0 2 * * ?") // 2 AM nightly
  │
  ├─ SELECT from customer_metrics WHERE monetaryTotal > 5000
  │   AND recencyDays > 180
  │
  ├─ For each whale:
  │   LLM: "Write a phone script for a luxury brand ambassador to call
  │          {name} (LTV: ${ltv}) who hasn't ordered in {days} days.
  │          Sound human, empathetic, never robotic."
  │
  └─ SlackNotificationService.send({
       "@Sales-Director: 🚨 Whale Alert!
        Client: {name} | LTV: ${ltv} | Silent: {days} days
        Call Script: {script}"
     })
```

---

### Phase 5: Multi-Agent Architecture

*Multiple AI personas debate each other to produce superhuman output.*

---

#### Feature 21 — Autonomous Budget Agent (The Fund Manager)

**What it does:** Runs hourly. Calculates the Return on Ad Spend (ROAS) of every RUNNING campaign. Automatically cuts the budget of losing campaigns (ROAS < 1.0) by 50% and redirects those dollars to the winning campaign — capturing viral momentum instantly.

**Files:** `CampaignFundManagerJob.java` (`@Scheduled cron="0 0 * * * ?"`)

**Architecture:**
```
CampaignFundManagerJob (every hour)
  │
  ├─ Fetch all RUNNING campaigns
  ├─ For each: ROAS = revenueAttributed / currentSpend
  ├─ Find: highestRoasCampaign, lowestRoasCampaign
  │
  └─ If lowestRoas < 1.0 (losing money):
      transferAmount = lowestCampaign.remainingBudget * 0.50
      lowestCampaign.budgetAllocated -= transferAmount  (DRAIN)
      highestCampaign.budgetAllocated += transferAmount  (BOOST)
      Save both → "Fund Manager transferred $X from Campaign A to Campaign B"
```

**Trigger for testing:**
```http
POST /api/v1/test/ai/trigger-fund-manager
```

---

#### Feature 22 — AI War Room (Multi-Agent Debate)

**What it does:** Three distinct AI personas debate a marketing strategy via sequential prompt chaining. Agent A (Aggressive CMO) proposes. Agent B (Conservative CFO) critiques. Agent C (CEO Negotiator) synthesises the final compromise. No single LLM hallucination goes unchallenged.

**Files:** `MultiAgentDebateService.java`

**Architecture:**
```
orchestrateDebate(campaignGoal: String)
  │
  ├─ Prompt A: "You are an aggressive CMO. Draft a strategy for: '{goal}'"
  │   → strategyA = callGemini(promptA)
  │
  ├─ Prompt B: "You are a conservative CFO. Critique this strategy: '{strategyA}'.
  │              Propose a conservative counter-strategy."
  │   → strategyB = callGemini(promptB)
  │
  └─ Prompt C: "You are the CEO. Synthesise these two into a balanced
  │              3-sentence compromise strategy."
  │   → finalCompromise = callGemini(promptC)
  │
  └─ Return full debate record (all 3 agents' outputs)
```

**Trigger:**
```http
POST /api/v1/test/ai/trigger-war-room
{ "goal": "Win back churned users with maximum urgency" }
Response: { "finalStrategy": "==== WAR ROOM DEBATE RECORD ===..." }
```

---

### Phase 6: Omni-Awareness

*The system protects itself and the relationship with customers.*

---

#### Feature 23 — Fatigue Engine (Channel Cooldown)

**What it does:** Prevents over-communication. Tracks open rate velocity per user. If a previously engaged user's open rate suddenly drops to 0% after receiving 4+ messages, their `channelCooldownUntil` is set to `NOW() + 14 days`. The dispatch engine strictly checks this lock before sending.

**Files:** `ChannelFatigueJob.java`, enforcement in `CampaignExecutionService.java`

**Architecture:**
```
ChannelFatigueJob (@Scheduled)
  │
  ├─ For each customer with recent high communication volume:
  │   IF recent_open_rate == 0% AND messages_last_7_days > 4:
  │     UPDATE customer_metrics SET channel_cooldown_until = NOW() + 14 DAYS
  │
CampaignExecutionService (dispatch loop)
  │
  └─ FOR EACH target customer:
      IF customer.metrics.channelCooldownUntil > NOW():
        SKIP → log("Skipped {customerId} due to fatigue cooldown")
```

**Trigger:**
```http
POST /api/v1/test/ai/trigger-omni-awareness
```

---

#### Feature 24 — Micro-Churn Velocity (The Whisperer)

**What it does:** Detects microscopic deviations in a customer's normal buying cadence — not full churn, but a 4-day lag in a 30-day purchase cycle. Deploys a human-sounding, non-salesy plain-text "check-in" message that conceals it is AI-generated.

**Files:** `MicroChurnWhispererJob.java`

**Architecture:**
```
MicroChurnWhispererJob (@Scheduled)
  │
  ├─ Compare customer.recencyDays vs their historical average purchase interval
  ├─ If lag > 15% above average AND churnRisk < 0.5 (not yet flagged):
  │   LLM: "Write a plain-text, human-sounding check-in message.
  │          No HTML. No discount. Looks like a personal email from an
  │          account manager. Max 60 words."
  └─ Send via ChannelDispatchService (plain text email)
```

---

## 📡 Complete API Reference

### Authentication
All requests require: `X-API-KEY: <your-key>` header

### Standard Response Envelope
```json
{
  "success": true,
  "data": { ... },
  "message": "OK",
  "pagination": { "totalPages": 5, "totalElements": 97, "page": 0 }
}
```

### Endpoints Summary

| Method | Path | Description |
|---|---|---|
| `POST` | `/customers` | Create customer |
| `PATCH` | `/customers/{id}` | Update customer |
| `GET` | `/customers` | List customers (paginated) |
| `GET` | `/customers/{id}` | Get customer |
| `POST` | `/customers/bulk` | Bulk create customers |
| `POST` | `/orders` | Create order + recalculate RFM |
| `GET` | `/orders` | List orders |
| `PATCH` | `/orders/{id}/status` | Update order status |
| `POST` | `/products` | Create product |
| `GET` | `/products` | List products |
| `PUT` | `/products/{id}` | Update product |
| `GET` | `/products/categories` | List categories |
| `POST` | `/segments` | Create segment |
| `GET` | `/segments` | List all segments |
| `PATCH` | `/segments/{id}` | Update segment |
| `DELETE` | `/segments/{id}` | Delete segment |
| `POST` | `/segments/{id}/evaluate` | Re-evaluate segment membership |
| `GET` | `/segments/{id}/members` | Get segment members |
| `GET` | `/segments/{id}/persona` | Generate AI buyer persona |
| `POST` | `/campaigns` | Create campaign |
| `GET` | `/campaigns` | List campaigns |
| `GET` | `/campaigns/{id}` | Get campaign details |
| `PUT` | `/campaigns/{id}` | Update campaign |
| `DELETE` | `/campaigns/{id}` | Delete campaign |
| `POST` | `/campaigns/{id}/approve` | Approve & execute campaign |
| `GET` | `/campaigns/proposals` | AI-generated proposals (DRAFT + createdByAgent) |
| `GET` | `/campaigns/{id}/timeline` | Campaign event timeline |
| `GET` | `/campaigns/{id}/analytics/narrative` | AI narrative analytics |
| `GET` | `/campaigns/opt-out-alerts` | Campaigns with high unsubscribe rates |
| `POST` | `/variants` | Create variant |
| `GET` | `/variants/campaign/{id}` | Get all variants for campaign |
| `PUT` | `/variants/{id}` | Update variant |
| `GET` | `/campaigns/{id}/variants/mab-stats` | Thompson Sampling stats |
| `GET` | `/simulations/campaigns/{id}/counterfactual` | Run simulation |
| `POST` | `/agent/chat` | Start agent session |
| `GET` | `/agent/sessions/{id}` | Poll session status |
| `GET` | `/agent/sessions/{id}/decisions` | Get reasoning trace |
| `GET` | `/memory` | All memory entries |
| `GET` | `/memory/query` | Filtered memory |
| `GET` | `/memory/ask` | Natural language memory query |
| `POST` | `/communications/send` | Send direct message |
| `GET` | `/audit-logs` | Audit log entries |
| `GET` | `/corrections` | Human feedback entries |
| `POST` | `/test/ai/trigger-war-room` | Trigger multi-agent debate |
| `POST` | `/test/ai/trigger-fund-manager` | Trigger budget rebalancing |
| `POST` | `/test/ai/trigger-omni-awareness` | Trigger fatigue engine |

---

## 🔄 End-to-End Data Flows

### Flow A: The Sovereign AI Agent — Full Lifecycle

```
1. Marketer types: "Create a win-back campaign for VIPs who haven't bought in 60 days"
   │
   POST /agent/chat { "prompt": "..." }
   │
2. AgentOrchestrationService.processChat()
   ├─ Create AgentSessionEntity (status=RUNNING)
   ├─ Return { sessionId: "abc-123", textReply: "Agent started..." }
   └─ @Async: runAsyncOrchestration(sessionId, prompt)
   │
3. runAsyncOrchestration() — runs in background thread pool
   │
   ├─ Step 1: REASON
   │   callGemini("Given goal: win-back VIPs. What is my plan?")
   │   → save AgentDecisionEntity (type=MEMORY_LOOKUP)
   │
   ├─ Step 2: MEMORY LOOKUP
   │   GET /memory?segmentTag=VIP
   │   → injects: "Email drives 2x conversion for VIPs vs SMS"
   │   → save AgentDecisionEntity
   │
   ├─ Step 3: SEGMENT SEARCH
   │   Query AudienceSegmentRepository for "VIP" segments
   │   → found: { id: "vip-segment-id", name: "VIP Customers" }
   │   → save AgentDecisionEntity (type=SEGMENT_SEARCH)
   │
   ├─ Step 4: GENERATE CAMPAIGN
   │   callGemini("Create a win-back campaign for VIP segment.
   │               Memory context: Email wins for VIPs.
   │               Return JSON: {name, goal, subjectLine, bodyHtml}")
   │   → parse JSON response
   │   → save CampaignEntity (status=DRAFT, createdByAgent=true)
   │   → save MessageVariantEntity
   │   → save AgentDecisionEntity (type=GENERATE_CAMPAIGN)
   │
   └─ Step 5: COMPLETE
       Update AgentSessionEntity (status=COMPLETED, plan={campaignId, name})
   │
4. Frontend polls GET /agent/sessions/abc-123 every 3 seconds
   → status changes RUNNING → COMPLETED
   → shows "Accept & Execute Campaign" button
   │
5. Marketer clicks "Accept"
   POST /campaigns/{campaignId}/approve
   │
6. CampaignExecutionService.executeCampaignAsync() [@Async]
   (See dispatch flow above)
   │
7. After execution: MemoryRetrievalService.extractAndSave()
   → new MemoryEntity saved for future agent sessions
```

### Flow B: Proposal Inbox — AI Churn Campaign Approved

```
PredictiveChurnJob (scheduled)
  → detects 50 high-risk customers
  → creates CampaignEntity (DRAFT, createdByAgent=true)
  → creates temp segment "Auto-Churn-Risk-2026-06-14"

Frontend proposals/page.tsx
  → GET /campaigns/proposals → returns this DRAFT campaign
  → Renders as swipeable card

Marketer clicks "Approve & Launch"
  → POST /campaigns/{id}/approve
  → Campaign status → RUNNING
  → 50 emails dispatched with:
     ├─ SmartRouting (email if open rate > 70%)
     ├─ HyperPersonalisation (unique body per user)
     └─ DynamicDiscount (churn risk 0.8 → 25% off)
  → proposals/page.tsx: toast "Approved! View Campaign →"
  → campaign removed from inbox (optimistic update)
```

### Flow C: MAB Tournament — Live Campaign Optimising Itself

```
Day 1: Campaign launched with Variant A and Variant B
  mabAlpha: [1.0, 1.0], mabBeta: [1.0, 1.0]

Day 1, Email 1 sent:
  MultiArmedBanditService samples Beta(1,1) for both
  Both return ~0.5, Variant A selected randomly

Day 2, 200 emails sent:
  Variant A: 40 opens (mabAlpha=41, mabBeta=161)
  Variant B: 80 opens (mabAlpha=81, mabBeta=121)
  Beta(81, 121) dominates → Variant B selected 70% of time

Day 3, VariantEvolutionJob runs:
  Variant B: mabConversions=100, mabAlpha > mabBeta * 1.5
  → WINNER detected
  → LLM breeds Variant C and Variant D from B
  → Variant A deactivated (mabIsActive=false)

Day 4: Campaign now running A/B/C/D tournament
  Thompson Sampling explores C and D
  → Variant C emerges as winner
  → Process repeats infinitely
```

---

## 🔐 Security Architecture

```
HTTP Request
  │
  ▼
ApiKeyAuthenticationFilter
  ├─ Extract X-API-KEY header
  ├─ Validate against XENO_API_KEY env variable
  ├─ If missing/invalid → 401 Unauthorized
  └─ If valid → UsernamePasswordAuthenticationToken → SecurityContext

SecurityConfig
  ├─ CORS: allowedOrigins=[frontend URL, localhost:3000]
  ├─ CSRF: disabled (stateless API)
  ├─ Session: STATELESS
  └─ All routes authenticated except /actuator/health

SQL Injection Prevention
  └─ SegmentQueryBuilder.buildQuery()
       → compiles filterJson to parameterized SQL
       → JdbcTemplate uses PreparedStatement
       → user-supplied values NEVER concatenated into SQL strings
```

---

## ⏰ Scheduled Jobs Reference

| Job | Schedule | Purpose | Output |
|---|---|---|---|
| `VariantEvolutionJob` | Every 1 hour | Thompson Sampling + LLM variant breeding | New `MessageVariantEntity` records |
| `CampaignFundManagerJob` | Every 1 hour | ROAS-based budget rebalancing | Updated `campaigns.budgetAllocated` |
| `JourneyFallbackService` | Every 5 minutes | Email→SMS fallback for unengaged users | New `CommunicationEntity` records |
| `PredictiveChurnJob` | Configurable | Churn risk detection + campaign drafting | DRAFT `CampaignEntity` in proposals |
| `ExternalTriggerJob` | Configurable | External signal → contextual campaign | DRAFT `CampaignEntity` in proposals |
| `PredictiveInventoryJob` | Configurable | Dead stock detection → clearance campaign | DRAFT `CampaignEntity` in proposals |
| `ZeroPartyLookalikeJob` | Configurable | AI SQL lookalike synthesis | New `AudienceSegmentEntity` |
| `ChannelFatigueJob` | Configurable | Communication exhaustion detection | Updated `channelCooldownUntil` |
| `VipConciergeEscalationJob` | `0 0 2 * * ?` (2 AM) | Whale churn → Slack escalation | Slack webhook POST |
| `MicroChurnWhispererJob` | Configurable | Micro-cadence deviation detection | New `CommunicationEntity` |

---

## 🚀 Local Development Setup

### Prerequisites

| Tool | Minimum Version |
|---|---|
| Java JDK | 17 |
| Maven | 3.8+ |
| Node.js | 18+ |
| npm | 9+ |
| PostgreSQL | 15+ |
| Git | 2.x |

### 1. Clone the repository

```bash
git clone https://github.com/LalithChowdary/Project-Xeno.git
cd Project-Xeno
```

### 2. Database Setup

```bash
# Create a PostgreSQL database
psql -U postgres
CREATE DATABASE xenocrm;
\q

# Flyway will auto-apply migrations on backend startup
# OR manually apply: psql -U postgres -d xenocrm -f database.sql
```

### 3. Backend Setup

```bash
cd xenon-backend

# Copy env template
cp src/main/resources/application-local.properties.example \
   src/main/resources/application-local.properties

# Edit application-local.properties — fill in your values
# (see Environment Variables section below)

# Build and run
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Backend starts at: `http://localhost:8080`

### 4. Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Create environment file
cat > .env.local << EOF
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
NEXT_PUBLIC_API_KEY=your-local-api-key
EOF

# Start dev server
npm run dev
```

Frontend starts at: `http://localhost:3000`

### 5. Optional: Channel Stub Server

A stub server simulates email/SMS sending without real credentials:

```bash
cd channel-stub
# Run the stub (Node.js)
node index.js
```

---

## 🔧 Environment Variables

### Backend (`application-local.properties`)

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/xenocrm
spring.datasource.username=postgres
spring.datasource.password=your_password

# AI
gemini.api.key=AIza...your_gemini_key

# Security
xeno.api.key=your_secret_api_key

# Slack (for VIP Escalation)
slack.webhook.url=https://hooks.slack.com/services/...

# Scheduling (disable for local dev if needed)
spring.task.scheduling.enabled=true
```

### Frontend (`.env.local`)

```bash
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
NEXT_PUBLIC_API_KEY=your_secret_api_key
```

---

## 📦 Deployment

### Docker (Backend)

```bash
# Build image
docker build -t project-xeno-backend .

# Run container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/xenocrm \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=secret \
  -e GEMINI_API_KEY=AIza... \
  -e XENO_API_KEY=your-key \
  project-xeno-backend
```

### Render.com (Backend)

1. Connect GitHub repository
2. Set build command: `./mvnw clean package -DskipTests`
3. Set start command: `java -jar target/xenon-backend-0.0.1-SNAPSHOT.jar`
4. Add all environment variables in Render dashboard

### Vercel (Frontend)

```bash
# Install Vercel CLI
npm i -g vercel

cd frontend
vercel deploy --prod
# Add NEXT_PUBLIC_API_URL and NEXT_PUBLIC_API_KEY in Vercel dashboard
```

### Production Build Verification

```bash
cd frontend && npm run build
# Should output: "✓ Compiled successfully" with all 15 routes
```

---

## 🗺️ Feature Roadmap

| Phase | Status | Features |
|---|---|---|
| Phase 0: Core CRM | ✅ Complete | Customer 360, RFM, Dynamic SQL Segments |
| Phase 1: Trust | ✅ Complete | Explainability, Simulator, NL Analytics, Memory |
| Phase 2: Autonomous | ✅ Complete | Persona Gen, A/B Evolution, Smart Routing, Hyper-Personal, Journey Builder, Churn, External Triggers, HITL |
| Phase 3: AGI | ✅ Complete | Inventory Clearance, Dynamic Pricing, Lookalike Synthesis |
| Phase 4: Escalation | ✅ Complete | VIP Concierge (Slack) |
| Phase 5: Multi-Agent | ✅ Complete | Fund Manager, AI War Room |
| Phase 6: Omni-Aware | ✅ Complete | Fatigue Engine, Micro-Churn Whisperer |
| Phase 7: Future | 🔜 Planned | Voice Agent, WhatsApp native, Real-time bidding |

---

## 📊 Project Statistics

| Metric | Count |
|---|---|
| Total AI Features | **24** |
| Backend Java Files | **80+** |
| Frontend Pages | **11** |
| API Endpoints | **50+** |
| Scheduled Background Jobs | **10** |
| Database Tables | **18** |
| LLM Integration Points | **12** |
| Lines of Code (Backend) | ~8,000 |
| Lines of Code (Frontend) | ~5,000 |

---

## 👨‍💻 Author

**Likhith Chowdary** — [@LikhithChowdary](https://github.com/Likhith623)

Built with ❤️, Java, TypeScript, and the belief that AI should make marketers superhuman, not redundant.

---

## 📄 License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

---

<div align="center">

**Project Xeno** — *Where AI meets CRM at the frontier of what's possible.*

⭐ Star this repository if you found it valuable

</div>
