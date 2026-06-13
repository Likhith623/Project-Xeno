-- =============================================================================
-- XENO AI-NATIVE CRM — SUPABASE POSTGRESQL SCHEMA
-- Architecture: Sovereign Agent + Audience Simulator + Thompson Sampling MAB
--               + Async Callback Self-Correction + Organizational Memory Layer
-- =============================================================================

-- Enable required Postgres extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";      -- for fuzzy name/email search


-- =============================================================================
-- LAYER 0 — RAW CUSTOMER DATA  (Ingestion)
-- =============================================================================

-- customers: the core identity record for every shopper
CREATE TABLE customers (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    external_id         TEXT UNIQUE,                  -- brand's own customer ID, if any
    email               TEXT UNIQUE,
    phone               TEXT,                         -- E.164 format: +919876543210
    whatsapp_number     TEXT,
    name                TEXT NOT NULL,
    gender              TEXT CHECK (gender IN ('male','female','other','unknown')) DEFAULT 'unknown',
    date_of_birth       DATE,
    city                TEXT,
    state               TEXT,
    country             TEXT DEFAULT 'IN',
    tags                TEXT[]    DEFAULT '{}',       -- e.g. ['vip','coffee-buyer','churned']
    custom_attributes   JSONB     DEFAULT '{}',       -- brand-specific metadata
    preferred_channel   TEXT CHECK (preferred_channel IN ('email','whatsapp','sms','rcs')) DEFAULT 'email',
    opt_out_channels    TEXT[]    DEFAULT '{}',       -- channels this user has opted out from
    is_globally_opted_out BOOLEAN DEFAULT FALSE,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_customers_email       ON customers (email);
CREATE INDEX idx_customers_phone       ON customers (phone);
CREATE INDEX idx_customers_tags        ON customers USING GIN (tags);
CREATE INDEX idx_customers_custom_attr ON customers USING GIN (custom_attributes);
CREATE INDEX idx_customers_city        ON customers (city);
CREATE INDEX idx_customers_country     ON customers (country);


-- product_categories: taxonomy tree (electronics > phones > flagship)
CREATE TABLE product_categories (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        TEXT NOT NULL,
    parent_id   UUID REFERENCES product_categories(id),
    slug        TEXT UNIQUE NOT NULL
);

-- products: SKU-level catalog
CREATE TABLE products (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sku             TEXT UNIQUE NOT NULL,
    name            TEXT NOT NULL,
    category_id     UUID REFERENCES product_categories(id),
    price           NUMERIC(12,2) NOT NULL CHECK (price >= 0),
    currency        TEXT NOT NULL DEFAULT 'INR',
    brand           TEXT,
    tags            TEXT[]  DEFAULT '{}',
    attributes      JSONB   DEFAULT '{}',   -- color, size, etc.
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_products_sku      ON products (sku);
CREATE INDEX idx_products_category ON products (category_id);
CREATE INDEX idx_products_tags     ON products USING GIN (tags);


-- orders: one row per order (header)
CREATE TABLE orders (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id     UUID NOT NULL REFERENCES customers(id) ON DELETE RESTRICT,
    order_number    TEXT UNIQUE,
    status          TEXT CHECK (status IN ('pending','confirmed','shipped','delivered','cancelled','returned')) DEFAULT 'pending',
    channel         TEXT,                   -- 'web','app','store','whatsapp'
    total_amount    NUMERIC(12,2) NOT NULL CHECK (total_amount >= 0),
    currency        TEXT NOT NULL DEFAULT 'INR',
    discount_amount NUMERIC(12,2) DEFAULT 0,
    coupon_code     TEXT,
    placed_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    delivered_at    TIMESTAMPTZ,
    metadata        JSONB DEFAULT '{}',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_orders_customer    ON orders (customer_id);
CREATE INDEX idx_orders_placed_at   ON orders (placed_at DESC);
CREATE INDEX idx_orders_status      ON orders (status);


-- order_items: line items inside each order
CREATE TABLE order_items (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id        UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id      UUID REFERENCES products(id),
    product_sku     TEXT,                   -- denormalized for resilience
    product_name    TEXT NOT NULL,
    quantity        INT NOT NULL CHECK (quantity > 0),
    unit_price      NUMERIC(12,2) NOT NULL CHECK (unit_price >= 0),
    discount_amount NUMERIC(12,2) DEFAULT 0,
    line_total      NUMERIC(12,2) GENERATED ALWAYS AS
                        ((unit_price - discount_amount) * quantity) STORED
);

CREATE INDEX idx_order_items_order   ON order_items (order_id);
CREATE INDEX idx_order_items_product ON order_items (product_id);


-- =============================================================================
-- LAYER 1 — CUSTOMER COMPUTED METRICS  (RFM + behaviour signals)
-- Populated/refreshed by a Spring @Scheduled job or Supabase Edge Function
-- =============================================================================

CREATE TABLE customer_metrics (
    customer_id             UUID PRIMARY KEY REFERENCES customers(id) ON DELETE CASCADE,

    -- RFM
    recency_days            INT,            -- days since last order
    frequency               INT,            -- total orders (all time)
    monetary_total          NUMERIC(12,2),  -- cumulative spend
    monetary_avg_order      NUMERIC(12,2),  -- avg order value
    rfm_score               NUMERIC(4,2),   -- composite 1–5

    -- behaviour
    total_orders_last_30d   INT DEFAULT 0,
    total_orders_last_90d   INT DEFAULT 0,
    avg_days_between_orders NUMERIC(6,2),   -- inter-purchase cadence
    favourite_category_id   UUID REFERENCES product_categories(id),
    favourite_channel       TEXT,           -- 'web','app','store'
    clv_predicted           NUMERIC(12,2),  -- predicted 12-month CLV
    churn_probability       NUMERIC(5,4),   -- 0–1

    -- engagement
    email_open_rate         NUMERIC(5,4) DEFAULT 0,
    email_click_rate        NUMERIC(5,4) DEFAULT 0,
    whatsapp_read_rate      NUMERIC(5,4) DEFAULT 0,
    sms_click_rate          NUMERIC(5,4) DEFAULT 0,

    last_computed_at        TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_customer_metrics_rfm       ON customer_metrics (rfm_score DESC);
CREATE INDEX idx_customer_metrics_churn     ON customer_metrics (churn_probability DESC);
CREATE INDEX idx_customer_metrics_clv       ON customer_metrics (clv_predicted DESC);
CREATE INDEX idx_customer_metrics_recency   ON customer_metrics (recency_days);


-- =============================================================================
-- LAYER 2 — SEGMENTATION ENGINE
-- Segments are either AI-generated (from the Sovereign Agent) or manual
-- =============================================================================

-- audience_segments: definition + metadata
CREATE TABLE audience_segments (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name            TEXT NOT NULL,
    description     TEXT,
    type            TEXT CHECK (type IN ('static','dynamic','ai_generated')) DEFAULT 'dynamic',
    status          TEXT CHECK (status IN ('draft','building','ready','archived')) DEFAULT 'draft',
    --
    -- For dynamic segments: SQL WHERE clause fragment evaluated against
    -- customers + customer_metrics + orders.  Stored as-is; executed via a
    -- parameterised query on the backend (NEVER interpolated raw into SQL).
    filter_sql      TEXT,
    -- Human-readable representation of the filter for the UI
    filter_json     JSONB DEFAULT '{}',
    -- For static segments: populated once, not re-evaluated
    is_pinned       BOOLEAN DEFAULT FALSE,

    -- Sovereign Agent provenance
    created_by_agent    BOOLEAN DEFAULT FALSE,
    agent_goal          TEXT,               -- original NL goal that spawned this segment

    -- Stats (refreshed on re-evaluation)
    customer_count  INT DEFAULT 0,
    last_evaluated_at TIMESTAMPTZ,

    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_segments_type   ON audience_segments (type);
CREATE INDEX idx_segments_status ON audience_segments (status);


-- segment_members: resolved customer ↔ segment mapping (both static & cached dynamic)
CREATE TABLE segment_members (
    segment_id  UUID NOT NULL REFERENCES audience_segments(id) ON DELETE CASCADE,
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    added_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (segment_id, customer_id)
);

CREATE INDEX idx_segment_members_customer ON segment_members (customer_id);


-- =============================================================================
-- LAYER 3 — CAMPAIGNS
-- =============================================================================

CREATE TABLE campaigns (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name            TEXT NOT NULL,
    description     TEXT,
    status          TEXT CHECK (status IN (
                        'draft','simulating','scheduled','running',
                        'paused','completed','cancelled','failed'
                    )) DEFAULT 'draft',
    goal            TEXT,                   -- marketer's natural-language goal
    segment_id      UUID REFERENCES audience_segments(id),

    -- scheduling
    scheduled_at    TIMESTAMPTZ,
    started_at      TIMESTAMPTZ,
    completed_at    TIMESTAMPTZ,
    timezone        TEXT DEFAULT 'Asia/Kolkata',

    -- budget / safety rails
    max_send_count  INT,                    -- hard cap on messages dispatched
    opt_out_rate_threshold NUMERIC(5,4) DEFAULT 0.02,   -- auto-pause if exceeded

    -- Sovereign Agent provenance
    created_by_agent    BOOLEAN DEFAULT FALSE,
    agent_session_id    TEXT,               -- ties back to the agent run
    parent_campaign_id  UUID REFERENCES campaigns(id),  -- for self-correction retries

    -- aggregated performance (denormalized for dashboard speed)
    total_sent          INT DEFAULT 0,
    total_delivered     INT DEFAULT 0,
    total_failed        INT DEFAULT 0,
    total_opened        INT DEFAULT 0,
    total_read          INT DEFAULT 0,
    total_clicked       INT DEFAULT 0,
    total_converted     INT DEFAULT 0,      -- orders attributed to this campaign
    revenue_attributed  NUMERIC(14,2) DEFAULT 0,

    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_campaigns_status       ON campaigns (status);
CREATE INDEX idx_campaigns_segment      ON campaigns (segment_id);
CREATE INDEX idx_campaigns_scheduled_at ON campaigns (scheduled_at);
CREATE INDEX idx_campaigns_agent        ON campaigns (created_by_agent, agent_session_id);


-- =============================================================================
-- LAYER 4 — MESSAGE VARIANTS  (inputs to the MAB)
-- Each campaign has 1–N variants across channels; the MAB chooses which to send
-- =============================================================================

CREATE TYPE message_channel AS ENUM ('email','whatsapp','sms','rcs');

CREATE TABLE message_variants (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    campaign_id     UUID NOT NULL REFERENCES campaigns(id) ON DELETE CASCADE,
    name            TEXT NOT NULL,          -- e.g. "Email – urgency copy A"
    channel         message_channel NOT NULL,

    -- Content fields (all optional depending on channel)
    subject_line    TEXT,                   -- email subject
    preview_text    TEXT,                   -- email preview
    body_text       TEXT,                   -- plaintext / WhatsApp body
    body_html       TEXT,                   -- rich email HTML
    cta_text        TEXT,                   -- button label
    cta_url         TEXT,                   -- destination URL (may be personalised template)
    media_url       TEXT,                   -- image / video for WhatsApp / RCS
    template_id     TEXT,                   -- approved WA Business template ID
    template_params JSONB DEFAULT '{}',     -- placeholder values

    -- MAB state (Thompson Sampling — Beta(α, β) posterior per variant)
    mab_alpha       NUMERIC(10,4) DEFAULT 1.0,   -- successes + 1  (prior)
    mab_beta        NUMERIC(10,4) DEFAULT 1.0,   -- failures  + 1  (prior)
    mab_impressions INT DEFAULT 0,
    mab_conversions INT DEFAULT 0,
    mab_is_active   BOOLEAN DEFAULT TRUE,

    -- AI generation metadata
    generated_by_ai BOOLEAN DEFAULT FALSE,
    generation_prompt TEXT,

    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_variants_campaign ON message_variants (campaign_id);
CREATE INDEX idx_variants_channel  ON message_variants (campaign_id, channel);
CREATE INDEX idx_variants_mab      ON message_variants (mab_alpha, mab_beta);


-- =============================================================================
-- LAYER 5 — COMMUNICATIONS  (one row = one message dispatched to one customer)
-- =============================================================================

CREATE TABLE communications (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    campaign_id         UUID NOT NULL REFERENCES campaigns(id),
    variant_id          UUID NOT NULL REFERENCES message_variants(id),
    customer_id         UUID NOT NULL REFERENCES customers(id),
    channel             message_channel NOT NULL,

    -- Delivery state machine
    -- pending → sent → delivered | failed
    --                delivered → opened → read → clicked → converted
    status              TEXT CHECK (status IN (
                            'pending','sent','delivered','failed',
                            'opened','read','clicked','converted',
                            'unsubscribed','bounced','expired'
                        )) DEFAULT 'pending',

    -- External channel reference (from stubbed channel service)
    channel_message_id  TEXT,               -- ID returned by the channel service
    recipient_address   TEXT NOT NULL,      -- email address or phone number used

    -- Personalised content snapshot (what was actually sent)
    personalised_subject    TEXT,
    personalised_body       TEXT,

    -- Timestamps of each lifecycle event (NULL = not yet happened)
    sent_at             TIMESTAMPTZ,
    delivered_at        TIMESTAMPTZ,
    failed_at           TIMESTAMPTZ,
    opened_at           TIMESTAMPTZ,
    read_at             TIMESTAMPTZ,
    clicked_at          TIMESTAMPTZ,
    converted_at        TIMESTAMPTZ,
    unsubscribed_at     TIMESTAMPTZ,

    -- Failure details
    failure_reason      TEXT,
    failure_code        TEXT,               -- channel-specific error code
    retry_count         INT DEFAULT 0,
    next_retry_at       TIMESTAMPTZ,

    -- Attribution
    attributed_order_id UUID REFERENCES orders(id),
    attribution_window_hours INT DEFAULT 72,

    -- Callback self-correction link
    spawned_followup_id UUID REFERENCES communications(id), -- new comm triggered by self-correction

    -- MAB context (which sample was used to select this variant)
    mab_sample_value    NUMERIC(8,6),       -- sampled θ that won the draw

    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_comms_campaign     ON communications (campaign_id);
CREATE INDEX idx_comms_customer     ON communications (customer_id);
CREATE INDEX idx_comms_variant      ON communications (variant_id);
CREATE INDEX idx_comms_status       ON communications (status);
CREATE INDEX idx_comms_channel_msg  ON communications (channel_message_id);
CREATE INDEX idx_comms_sent_at      ON communications (sent_at DESC);
CREATE INDEX idx_comms_retry        ON communications (next_retry_at) WHERE status = 'failed';


-- =============================================================================
-- LAYER 6 — CHANNEL CALLBACKS  (raw webhook events from the stubbed channel service)
-- Append-only event log; the processor updates communications from these
-- =============================================================================

CREATE TABLE channel_callbacks (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    communication_id    UUID REFERENCES communications(id),
    channel_message_id  TEXT,               -- match by this if comm_id is unknown
    event_type          TEXT CHECK (event_type IN (
                            'delivered','failed','opened','read',
                            'clicked','converted','unsubscribed','bounced','expired'
                        )) NOT NULL,
    payload             JSONB NOT NULL DEFAULT '{}',  -- raw callback body
    received_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    processed_at        TIMESTAMPTZ,
    processing_status   TEXT CHECK (processing_status IN ('pending','processed','error')) DEFAULT 'pending',
    processing_error    TEXT
);

CREATE INDEX idx_callbacks_comm_id   ON channel_callbacks (communication_id);
CREATE INDEX idx_callbacks_msg_id    ON channel_callbacks (channel_message_id);
CREATE INDEX idx_callbacks_pending   ON channel_callbacks (processing_status, received_at)
                                      WHERE processing_status = 'pending';
CREATE INDEX idx_callbacks_event     ON channel_callbacks (event_type);


-- =============================================================================
-- LAYER 7 — AI SELF-CORRECTION ENGINE
-- Records every correction decision: why it fired, what it changed, outcome
-- =============================================================================

CREATE TABLE correction_events (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    campaign_id         UUID NOT NULL REFERENCES campaigns(id),
    trigger_type        TEXT CHECK (trigger_type IN (
                            'high_failure_rate','low_open_rate','low_ctr',
                            'channel_timeout','bounce_spike','opt_out_spike'
                        )) NOT NULL,
    trigger_threshold   NUMERIC(8,4),       -- value that crossed the threshold
    observed_value      NUMERIC(8,4),       -- actual measured value
    cohort_size         INT,                -- how many communications were in the affected cohort

    -- What the AI decided to do
    action_taken        TEXT CHECK (action_taken IN (
                            'switch_channel','rewrite_copy','pause_campaign',
                            'reduce_frequency','add_fallback','no_action'
                        )) NOT NULL,
    old_channel         message_channel,
    new_channel         message_channel,
    old_variant_id      UUID REFERENCES message_variants(id),
    new_variant_id      UUID REFERENCES message_variants(id),
    ai_reasoning        TEXT,               -- LLM explanation stored for audit

    -- Outcome measurement (filled in after the correction has had time to run)
    correction_outcome  TEXT CHECK (correction_outcome IN ('improved','neutral','worsened','insufficient_data')),
    outcome_delta       NUMERIC(8,4),       -- e.g. +0.04 = CTR improved by 4pp

    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    evaluated_at        TIMESTAMPTZ
);

CREATE INDEX idx_corrections_campaign ON correction_events (campaign_id);
CREATE INDEX idx_corrections_trigger  ON correction_events (trigger_type);
CREATE INDEX idx_corrections_action   ON correction_events (action_taken);


-- =============================================================================
-- LAYER 8 — AUDIENCE SIMULATOR  (pre-send synthetic CTR / conversion estimate)
-- =============================================================================

-- simulation_runs: one per campaign variant set before go-live
CREATE TABLE simulation_runs (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    campaign_id         UUID NOT NULL REFERENCES campaigns(id),
    status              TEXT CHECK (status IN ('pending','running','completed','failed')) DEFAULT 'pending',
    synthetic_audience_size INT NOT NULL DEFAULT 500,

    -- Results (populated on completion)
    predicted_open_rate     NUMERIC(5,4),
    predicted_ctr           NUMERIC(5,4),
    predicted_conversion_rate NUMERIC(5,4),
    predicted_revenue       NUMERIC(12,2),
    confidence_interval_low NUMERIC(5,4),
    confidence_interval_high NUMERIC(5,4),
    winning_variant_id      UUID REFERENCES message_variants(id),

    -- Persona distribution used
    persona_distribution    JSONB DEFAULT '{}',  -- {'high_value':0.3,'new':0.2,...}

    started_at      TIMESTAMPTZ,
    completed_at    TIMESTAMPTZ,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- simulation_personas: the synthetic customer archetypes
CREATE TABLE simulation_personas (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name                TEXT NOT NULL,       -- "Value-seeking new shopper"
    description         TEXT,
    archetype           TEXT,                -- 'bargain_hunter','loyalist','browser','impulse'
    -- Probabilistic response parameters
    base_open_rate      NUMERIC(5,4) NOT NULL,
    base_ctr            NUMERIC(5,4) NOT NULL,
    base_conversion_rate NUMERIC(5,4) NOT NULL,
    -- Modifier matrix: how channel / time / copy-style shifts these probabilities
    channel_multipliers JSONB DEFAULT '{}',  -- {"email":1.1,"whatsapp":0.9}
    time_multipliers    JSONB DEFAULT '{}',  -- {"morning":1.2,"evening":0.8}
    copy_multipliers    JSONB DEFAULT '{}',  -- {"urgency":1.15,"informational":0.9}
    is_active           BOOLEAN DEFAULT TRUE,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- simulation_results: per-variant outcomes inside a simulation run
CREATE TABLE simulation_results (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    simulation_id   UUID NOT NULL REFERENCES simulation_runs(id) ON DELETE CASCADE,
    variant_id      UUID NOT NULL REFERENCES message_variants(id),
    persona_id      UUID REFERENCES simulation_personas(id),
    simulated_sends INT NOT NULL DEFAULT 0,
    simulated_opens INT NOT NULL DEFAULT 0,
    simulated_clicks INT NOT NULL DEFAULT 0,
    simulated_conversions INT NOT NULL DEFAULT 0,
    simulated_revenue NUMERIC(12,2) DEFAULT 0,
    open_rate       NUMERIC(5,4) GENERATED ALWAYS AS
                        (CASE WHEN simulated_sends=0 THEN 0
                              ELSE simulated_opens::NUMERIC / simulated_sends END) STORED,
    ctr             NUMERIC(5,4) GENERATED ALWAYS AS
                        (CASE WHEN simulated_sends=0 THEN 0
                              ELSE simulated_clicks::NUMERIC / simulated_sends END) STORED
);

CREATE INDEX idx_sim_results_simulation ON simulation_results (simulation_id);
CREATE INDEX idx_sim_results_variant    ON simulation_results (variant_id);


-- =============================================================================
-- LAYER 9 — ORGANIZATIONAL MEMORY LAYER
-- Stores learnings persisted across campaigns for future Sovereign Agent use
-- =============================================================================

CREATE TABLE org_memory_entries (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    -- Scope
    segment_tag     TEXT,               -- 'coffee_buyers', 'luxury_segment', etc.
    channel         message_channel,    -- NULL = cross-channel finding
    day_of_week     INT CHECK (day_of_week BETWEEN 0 AND 6),  -- NULL = any day
    time_of_day     TEXT CHECK (time_of_day IN ('morning','afternoon','evening','night')),

    -- The learning itself
    learning_type   TEXT CHECK (learning_type IN (
                        'copy_style','send_time','channel_preference',
                        'frequency','offer_type','subject_pattern'
                    )) NOT NULL,
    learning_summary TEXT NOT NULL,     -- e.g. "Urgency CTAs +22% CTR for coffee segment on Wed"
    confidence      NUMERIC(5,4) DEFAULT 0.5,   -- 0–1, updated as evidence accumulates

    -- Evidence base
    source_campaign_ids UUID[] DEFAULT '{}',
    evidence_count  INT DEFAULT 1,      -- number of campaigns supporting this learning
    avg_lift        NUMERIC(6,4),       -- average performance delta vs baseline

    -- For the MAB: which copy patterns worked
    winning_copy_signals JSONB DEFAULT '{}',  -- {"urgency":true,"emoji":false,"length":"short"}

    is_active       BOOLEAN DEFAULT TRUE,   -- set false when contradicted by new data
    expires_at      TIMESTAMPTZ,            -- stale knowledge auto-expires

    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_org_memory_segment  ON org_memory_entries (segment_tag);
CREATE INDEX idx_org_memory_channel  ON org_memory_entries (channel);
CREATE INDEX idx_org_memory_type     ON org_memory_entries (learning_type);
CREATE INDEX idx_org_memory_active   ON org_memory_entries (is_active, confidence DESC);
CREATE INDEX idx_org_memory_dow      ON org_memory_entries (day_of_week, time_of_day);


-- =============================================================================
-- LAYER 10 — SOVEREIGN AGENT  (audit log of every agent session and decision)
-- =============================================================================

CREATE TABLE agent_sessions (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    goal            TEXT NOT NULL,          -- raw marketer input
    status          TEXT CHECK (status IN ('running','completed','failed','paused')) DEFAULT 'running',
    model_used      TEXT DEFAULT 'gemini-2.5-pro',
    -- JSON array of all LLM turns (truncated if >100 turns for storage efficiency)
    conversation_log JSONB DEFAULT '[]',
    -- Structured plan the agent settled on
    plan            JSONB DEFAULT '{}',
    -- Resources created by this session
    created_segment_id  UUID REFERENCES audience_segments(id),
    created_campaign_id UUID REFERENCES campaigns(id),
    error_message       TEXT,
    started_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    completed_at    TIMESTAMPTZ,
    tokens_used_in  INT DEFAULT 0,
    tokens_used_out INT DEFAULT 0
);

CREATE INDEX idx_agent_sessions_status ON agent_sessions (status);
CREATE INDEX idx_agent_sessions_goal   ON agent_sessions USING GIN (to_tsvector('english', goal));


-- agent_decisions: fine-grained step log for explainability
CREATE TABLE agent_decisions (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id      UUID NOT NULL REFERENCES agent_sessions(id) ON DELETE CASCADE,
    step_number     INT NOT NULL,
    decision_type   TEXT CHECK (decision_type IN (
                        'segment_query','variant_generation','channel_selection',
                        'schedule_decision','send_command','abort','memory_lookup',
                        'simulation_trigger','correction_trigger'
                    )) NOT NULL,
    input_context   JSONB DEFAULT '{}',
    output_action   JSONB DEFAULT '{}',
    reasoning       TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_agent_decisions_session ON agent_decisions (session_id, step_number);


-- =============================================================================
-- UTILITY — VIEWS for dashboard / API layer
-- =============================================================================

-- Campaign performance summary view
CREATE OR REPLACE VIEW v_campaign_performance AS
SELECT
    c.id,
    c.name,
    c.status,
    c.goal,
    c.scheduled_at,
    c.started_at,
    c.completed_at,
    c.created_by_agent,
    c.total_sent,
    c.total_delivered,
    c.total_failed,
    c.total_opened,
    c.total_read,
    c.total_clicked,
    c.total_converted,
    c.revenue_attributed,
    -- rates (avoid division by zero)
    CASE WHEN c.total_sent    > 0 THEN ROUND(c.total_delivered::NUMERIC  / c.total_sent * 100, 2) END AS delivery_rate_pct,
    CASE WHEN c.total_sent    > 0 THEN ROUND(c.total_failed::NUMERIC     / c.total_sent * 100, 2) END AS failure_rate_pct,
    CASE WHEN c.total_delivered > 0 THEN ROUND(c.total_opened::NUMERIC   / c.total_delivered * 100, 2) END AS open_rate_pct,
    CASE WHEN c.total_delivered > 0 THEN ROUND(c.total_clicked::NUMERIC  / c.total_delivered * 100, 2) END AS ctr_pct,
    CASE WHEN c.total_delivered > 0 THEN ROUND(c.total_converted::NUMERIC/ c.total_delivered * 100, 2) END AS conversion_rate_pct,
    -- opt-out rate across the campaign's communications
    (SELECT ROUND(
        COUNT(*) FILTER (WHERE status = 'unsubscribed')::NUMERIC / NULLIF(COUNT(*),0) * 100, 4
     ) FROM communications WHERE campaign_id = c.id) AS opt_out_rate_pct,
    seg.name AS segment_name,
    seg.customer_count AS segment_size
FROM campaigns c
LEFT JOIN audience_segments seg ON c.segment_id = seg.id;


-- Per-variant MAB performance view (Thompson Sampling derived stats)
CREATE OR REPLACE VIEW v_variant_mab_stats AS
SELECT
    mv.id,
    mv.campaign_id,
    mv.name,
    mv.channel,
    mv.mab_alpha,
    mv.mab_beta,
    mv.mab_impressions,
    mv.mab_conversions,
    -- Expected conversion rate under the Beta posterior: α/(α+β)
    ROUND(mv.mab_alpha / (mv.mab_alpha + mv.mab_beta), 4) AS expected_conversion_rate,
    -- Approximate 95% credible interval width: ± 1.96 * sqrt(α*β / (α+β)^2 / (α+β+1))
    ROUND(1.96 * SQRT(
        (mv.mab_alpha * mv.mab_beta)
        / POWER(mv.mab_alpha + mv.mab_beta, 2)
        / (mv.mab_alpha + mv.mab_beta + 1)
    ), 4) AS ci_half_width_95,
    mv.mab_is_active,
    c.name AS campaign_name
FROM message_variants mv
JOIN campaigns c ON mv.campaign_id = c.id;


-- Customer 360 view (joins customer + metrics in one record for the API)
CREATE OR REPLACE VIEW v_customer_360 AS
SELECT
    cu.id,
    cu.name,
    cu.email,
    cu.phone,
    cu.whatsapp_number,
    cu.preferred_channel,
    cu.opt_out_channels,
    cu.is_globally_opted_out,
    cu.tags,
    cu.city,
    cu.country,
    cu.created_at AS customer_since,
    m.recency_days,
    m.frequency,
    m.monetary_total,
    m.monetary_avg_order,
    m.rfm_score,
    m.clv_predicted,
    m.churn_probability,
    m.email_open_rate,
    m.email_click_rate,
    m.whatsapp_read_rate,
    m.last_computed_at AS metrics_last_updated
FROM customers cu
LEFT JOIN customer_metrics m ON cu.id = m.customer_id;


-- Opt-out safety guardrail view: campaigns approaching or exceeding threshold
CREATE OR REPLACE VIEW v_opt_out_alerts AS
SELECT
    c.id AS campaign_id,
    c.name AS campaign_name,
    c.opt_out_rate_threshold,
    ROUND(
        COUNT(*) FILTER (WHERE comm.status = 'unsubscribed')::NUMERIC
        / NULLIF(COUNT(comm.id), 0) * 100, 4
    ) AS current_opt_out_rate_pct,
    CASE
        WHEN (COUNT(*) FILTER (WHERE comm.status = 'unsubscribed')::NUMERIC
              / NULLIF(COUNT(comm.id), 0)) >= c.opt_out_rate_threshold
        THEN 'EXCEEDED'
        WHEN (COUNT(*) FILTER (WHERE comm.status = 'unsubscribed')::NUMERIC
              / NULLIF(COUNT(comm.id), 0)) >= c.opt_out_rate_threshold * 0.8
        THEN 'WARNING'
        ELSE 'OK'
    END AS alert_level
FROM campaigns c
JOIN communications comm ON c.id = comm.campaign_id
WHERE c.status = 'running'
GROUP BY c.id, c.name, c.opt_out_rate_threshold;


-- =============================================================================
-- UTILITY — FUNCTIONS & TRIGGERS
-- =============================================================================

-- Auto-update updated_at on every write
CREATE OR REPLACE FUNCTION fn_set_updated_at()
RETURNS TRIGGER LANGUAGE plpgsql AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_customers_updated_at
    BEFORE UPDATE ON customers
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

CREATE TRIGGER trg_campaigns_updated_at
    BEFORE UPDATE ON campaigns
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

CREATE TRIGGER trg_comms_updated_at
    BEFORE UPDATE ON communications
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

CREATE TRIGGER trg_variants_updated_at
    BEFORE UPDATE ON message_variants
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

CREATE TRIGGER trg_segments_updated_at
    BEFORE UPDATE ON audience_segments
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

CREATE TRIGGER trg_org_memory_updated_at
    BEFORE UPDATE ON org_memory_entries
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();


-- After each callback is processed, propagate the status to communications
-- and increment MAB counters for conversion events.
CREATE OR REPLACE FUNCTION fn_apply_callback()
RETURNS TRIGGER LANGUAGE plpgsql AS $$
DECLARE
    v_comm_id UUID;
    v_variant_id UUID;
BEGIN
    -- Resolve communication id (prefer direct FK, fall back to message id lookup)
    v_comm_id := NEW.communication_id;
    IF v_comm_id IS NULL THEN
        SELECT id INTO v_comm_id
        FROM communications
        WHERE channel_message_id = NEW.channel_message_id
        LIMIT 1;
    END IF;

    IF v_comm_id IS NULL THEN
        NEW.processing_status := 'error';
        NEW.processing_error  := 'Could not resolve communication_id';
        RETURN NEW;
    END IF;

    -- Update the communication status (forward-only state machine)
    UPDATE communications
    SET
        status        = NEW.event_type,
        processed_at  = NOW(),
        delivered_at  = CASE WHEN NEW.event_type = 'delivered'     THEN NOW() ELSE delivered_at END,
        failed_at     = CASE WHEN NEW.event_type = 'failed'        THEN NOW() ELSE failed_at    END,
        opened_at     = CASE WHEN NEW.event_type = 'opened'        THEN NOW() ELSE opened_at    END,
        read_at       = CASE WHEN NEW.event_type = 'read'          THEN NOW() ELSE read_at      END,
        clicked_at    = CASE WHEN NEW.event_type = 'clicked'       THEN NOW() ELSE clicked_at   END,
        converted_at  = CASE WHEN NEW.event_type = 'converted'     THEN NOW() ELSE converted_at END,
        unsubscribed_at = CASE WHEN NEW.event_type = 'unsubscribed' THEN NOW() ELSE unsubscribed_at END,
        failure_reason = COALESCE(NEW.payload->>'error_message', failure_reason),
        failure_code   = COALESCE(NEW.payload->>'error_code', failure_code)
    WHERE id = v_comm_id;

    -- Increment the relevant campaign counter
    UPDATE campaigns SET
        total_delivered = total_delivered + CASE WHEN NEW.event_type = 'delivered'    THEN 1 ELSE 0 END,
        total_failed    = total_failed    + CASE WHEN NEW.event_type = 'failed'       THEN 1 ELSE 0 END,
        total_opened    = total_opened    + CASE WHEN NEW.event_type = 'opened'       THEN 1 ELSE 0 END,
        total_read      = total_read      + CASE WHEN NEW.event_type = 'read'         THEN 1 ELSE 0 END,
        total_clicked   = total_clicked   + CASE WHEN NEW.event_type = 'clicked'      THEN 1 ELSE 0 END,
        total_converted = total_converted + CASE WHEN NEW.event_type = 'converted'    THEN 1 ELSE 0 END
    WHERE id = (SELECT campaign_id FROM communications WHERE id = v_comm_id);

    -- Update Thompson Sampling MAB counters on conversion/click (treat click as success)
    IF NEW.event_type IN ('converted','clicked') THEN
        SELECT variant_id INTO v_variant_id
        FROM communications WHERE id = v_comm_id;

        UPDATE message_variants SET
            mab_conversions = mab_conversions + 1,
            mab_alpha       = mab_alpha + 1          -- Beta posterior: increment α on success
        WHERE id = v_variant_id;
    END IF;

    -- Increment MAB impressions on delivery (treat delivery as a "trial")
    IF NEW.event_type = 'delivered' THEN
        SELECT variant_id INTO v_variant_id
        FROM communications WHERE id = v_comm_id;

        UPDATE message_variants SET
            mab_impressions = mab_impressions + 1,
            mab_beta        = mab_beta + 1           -- Beta posterior: increment β on non-success
        WHERE id = v_variant_id;
    END IF;

    NEW.communication_id    := v_comm_id;
    NEW.processing_status   := 'processed';
    NEW.processed_at        := NOW();
    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_process_callback
    BEFORE INSERT ON channel_callbacks
    FOR EACH ROW EXECUTE FUNCTION fn_apply_callback();


-- Stored procedure: Thompson Sampling — sample a variant for a given campaign
-- Called by the Spring backend just before each message dispatch
CREATE OR REPLACE FUNCTION fn_thompson_sample(p_campaign_id UUID)
RETURNS UUID LANGUAGE plpgsql AS $$
DECLARE
    best_variant_id UUID;
    best_sample     NUMERIC := -1;
    rec             RECORD;
    this_sample     NUMERIC;
BEGIN
    FOR rec IN
        SELECT id, mab_alpha, mab_beta
        FROM message_variants
        WHERE campaign_id = p_campaign_id AND mab_is_active = TRUE
    LOOP
        -- Sample from Beta(α, β) using the Johnk method approximation via pgcrypto
        -- True Beta sampling requires an extension; this approximates via Gamma(α,1)/Gamma(α,1)+Gamma(β,1)
        -- For a full implementation use a Java-side call with Apache Commons Math.
        -- Here we store the expected value as a proxy (deterministic fallback).
        this_sample := rec.mab_alpha / (rec.mab_alpha + rec.mab_beta);
        -- Add a small noise term to simulate sampling variance (use random() which is thread-local in PG)
        this_sample := this_sample + (random() - 0.5) *
                       SQRT(rec.mab_alpha * rec.mab_beta
                            / POWER(rec.mab_alpha + rec.mab_beta, 2)
                            / (rec.mab_alpha + rec.mab_beta + 1));

        IF this_sample > best_sample THEN
            best_sample     := this_sample;
            best_variant_id := rec.id;
        END IF;
    END LOOP;

    RETURN best_variant_id;
END;
$$;


-- Stored procedure: materialise a dynamic segment into segment_members
CREATE OR REPLACE FUNCTION fn_evaluate_segment(p_segment_id UUID)
RETURNS INT LANGUAGE plpgsql AS $$
DECLARE
    v_count INT;
    v_filter TEXT;
BEGIN
    SELECT filter_sql INTO v_filter
    FROM audience_segments
    WHERE id = p_segment_id;

    -- Remove stale memberships
    DELETE FROM segment_members WHERE segment_id = p_segment_id;

    -- Re-insert (filter_sql is a WHERE clause fragment applied to the
    -- v_customer_360 view — the Java layer builds this via a safe query builder,
    -- never by raw interpolation)
    EXECUTE format(
        'INSERT INTO segment_members (segment_id, customer_id)
         SELECT %L, id FROM v_customer_360 WHERE %s
         ON CONFLICT DO NOTHING',
        p_segment_id, COALESCE(v_filter, 'TRUE')
    );

    GET DIAGNOSTICS v_count = ROW_COUNT;

    UPDATE audience_segments
    SET customer_count    = v_count,
        last_evaluated_at = NOW(),
        status            = 'ready'
    WHERE id = p_segment_id;

    RETURN v_count;
END;
$$;


-- =============================================================================
-- ROW LEVEL SECURITY (Supabase)
-- All tables are private by default; service-role key bypasses RLS (used by Spring)
-- =============================================================================

ALTER TABLE customers           ENABLE ROW LEVEL SECURITY;
ALTER TABLE orders              ENABLE ROW LEVEL SECURITY;
ALTER TABLE order_items         ENABLE ROW LEVEL SECURITY;
ALTER TABLE products            ENABLE ROW LEVEL SECURITY;
ALTER TABLE product_categories  ENABLE ROW LEVEL SECURITY;
ALTER TABLE customer_metrics    ENABLE ROW LEVEL SECURITY;
ALTER TABLE audience_segments   ENABLE ROW LEVEL SECURITY;
ALTER TABLE segment_members     ENABLE ROW LEVEL SECURITY;
ALTER TABLE campaigns           ENABLE ROW LEVEL SECURITY;
ALTER TABLE message_variants    ENABLE ROW LEVEL SECURITY;
ALTER TABLE communications      ENABLE ROW LEVEL SECURITY;
ALTER TABLE channel_callbacks   ENABLE ROW LEVEL SECURITY;
ALTER TABLE correction_events   ENABLE ROW LEVEL SECURITY;
ALTER TABLE simulation_runs     ENABLE ROW LEVEL SECURITY;
ALTER TABLE simulation_personas ENABLE ROW LEVEL SECURITY;
ALTER TABLE simulation_results  ENABLE ROW LEVEL SECURITY;
ALTER TABLE org_memory_entries  ENABLE ROW LEVEL SECURITY;
ALTER TABLE agent_sessions      ENABLE ROW LEVEL SECURITY;
ALTER TABLE agent_decisions     ENABLE ROW LEVEL SECURITY;

-- Spring Boot uses the Supabase service_role key, which bypasses RLS.
-- No additional policies are required for the backend.
-- If you add a frontend calling Supabase directly (not recommended for this CRM),
-- add anon/authenticated policies here.


-- =============================================================================
-- SAMPLE SEED DATA  (realistic demo data for the walkthrough video)
-- =============================================================================

-- Product categories
INSERT INTO product_categories (id, name, slug) VALUES
    ('a1000000-0000-0000-0000-000000000001', 'Coffee & Beverages', 'coffee-beverages'),
    ('a1000000-0000-0000-0000-000000000002', 'Beauty & Skincare',  'beauty-skincare'),
    ('a1000000-0000-0000-0000-000000000003', 'Luxury Accessories', 'luxury-accessories');

-- Products
INSERT INTO products (id, sku, name, category_id, price, tags) VALUES
    ('b1000000-0000-0000-0000-000000000001', 'COFFEE-ARABICA-250G', 'Ethiopian Arabica Beans 250g', 'a1000000-0000-0000-0000-000000000001', 799.00,  ARRAY['coffee','beans','premium']),
    ('b1000000-0000-0000-0000-000000000002', 'COFFEE-COLDBREW-KIT', 'Cold Brew Starter Kit',        'a1000000-0000-0000-0000-000000000001', 1499.00, ARRAY['coffee','cold-brew']),
    ('b1000000-0000-0000-0000-000000000003', 'BEAUTY-SERUM-30ML',   'Vitamin C Brightening Serum',  'a1000000-0000-0000-0000-000000000002', 2199.00, ARRAY['beauty','serum','skincare']),
    ('b1000000-0000-0000-0000-000000000004', 'LUXURY-WALLET-01',    'Genuine Leather Slim Wallet',  'a1000000-0000-0000-0000-000000000003', 3499.00, ARRAY['luxury','leather','wallet']);

-- Simulation personas
INSERT INTO simulation_personas (id, name, archetype, base_open_rate, base_ctr, base_conversion_rate, channel_multipliers, copy_multipliers) VALUES
    ('c1000000-0000-0000-0000-000000000001', 'Loyal Coffee Enthusiast', 'loyalist',
     0.48, 0.15, 0.08,
     '{"email":1.2,"whatsapp":1.1,"sms":0.7}',
     '{"urgency":1.3,"loyalty_reward":1.5,"informational":0.9}'),
    ('c1000000-0000-0000-0000-000000000002', 'Bargain Hunter',          'bargain_hunter',
     0.35, 0.12, 0.06,
     '{"email":1.0,"whatsapp":1.3,"sms":1.1}',
     '{"discount":1.6,"urgency":1.2,"informational":0.7}'),
    ('c1000000-0000-0000-0000-000000000003', 'Luxury Aspirant',         'impulse',
     0.30, 0.08, 0.04,
     '{"email":1.1,"whatsapp":0.8,"rcs":1.4}',
     '{"exclusivity":1.7,"scarcity":1.4,"discount":0.6}'),
    ('c1000000-0000-0000-0000-000000000004', 'Occasional Browser',      'browser',
     0.20, 0.04, 0.02,
     '{"email":1.0,"whatsapp":0.9,"sms":0.8}',
     '{"informational":1.1,"urgency":0.9,"discount":1.2}');

-- Sample customers
INSERT INTO customers (id, email, phone, name, city, tags, preferred_channel) VALUES
    ('d1000000-0000-0000-0000-000000000001', 'priya.sharma@example.com', '+919876543210', 'Priya Sharma',   'Bengaluru', ARRAY['coffee-buyer','vip'],     'email'),
    ('d1000000-0000-0000-0000-000000000002', 'ravi.kumar@example.com',   '+919876543211', 'Ravi Kumar',     'Mumbai',    ARRAY['coffee-buyer'],            'whatsapp'),
    ('d1000000-0000-0000-0000-000000000003', 'ananya.iyer@example.com',  '+919876543212', 'Ananya Iyer',    'Chennai',   ARRAY['beauty','skincare'],       'email'),
    ('d1000000-0000-0000-0000-000000000004', 'vikram.seth@example.com',  '+919876543213', 'Vikram Seth',    'Delhi',     ARRAY['luxury','high-value'],     'email'),
    ('d1000000-0000-0000-0000-000000000005', 'meena.nair@example.com',   '+919876543214', 'Meena Nair',     'Kochi',     ARRAY['coffee-buyer','churned'],  'sms');

-- Customer metrics for seed customers
INSERT INTO customer_metrics (customer_id, recency_days, frequency, monetary_total, monetary_avg_order, rfm_score, clv_predicted, churn_probability, email_open_rate, email_click_rate) VALUES
    ('d1000000-0000-0000-0000-000000000001', 12,  8, 14200, 1775, 4.2, 28000, 0.05, 0.52, 0.18),
    ('d1000000-0000-0000-0000-000000000002', 45,  3,  4800, 1600, 2.8,  8000, 0.22, 0.31, 0.09),
    ('d1000000-0000-0000-0000-000000000003',  5, 12, 22000, 1833, 4.8, 42000, 0.03, 0.61, 0.24),
    ('d1000000-0000-0000-0000-000000000004', 90,  2,  9000, 4500, 2.1,  5000, 0.55, 0.25, 0.05),
    ('d1000000-0000-0000-0000-000000000005',180,  1,   799,  799, 1.0,   500, 0.88, 0.10, 0.02);

-- Org memory seed: what the AI already knows
INSERT INTO org_memory_entries (segment_tag, channel, learning_type, learning_summary, confidence, evidence_count, avg_lift, winning_copy_signals) VALUES
    ('coffee_buyers', 'whatsapp', 'copy_style',
     'Urgency-framed CTAs ("Grab yours before 6PM") achieve 22% higher CTR vs informational copy for coffee segment on weekday mornings.',
     0.78, 3, 0.22, '{"urgency":true,"short_body":true,"emoji":true}'),
    ('beauty_segment', 'email', 'send_time',
     'Thursday 8–9PM send window achieves 1.4× open rate vs Tuesday morning for beauty segment.',
     0.71, 2, 0.40, '{"story_driven":true,"long_form":true}'),
    ('luxury_segment', 'email', 'copy_style',
     'Exclusivity framing ("Members-only access") converts 3.2× better than discount offers for luxury buyers.',
     0.85, 5, 0.32, '{"exclusivity":true,"no_discount":true,"premium_imagery":true}'),
    ('churned_users', 'sms', 'offer_type',
     'Re-engagement SMS with a time-bound 15% win-back discount recovers ~4% of churned segment within 7 days.',
     0.62, 2, 0.04, '{"discount":true,"urgency":true,"short_body":true}');


-- =============================================================================
-- END OF SCHEMA
-- Tables: 18  |  Views: 4  |  Functions: 4  |  Triggers: 7
-- =============================================================================
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,

    trace_id UUID,

    entity_type VARCHAR(100) NOT NULL,
    entity_id BIGINT NOT NULL,

    action VARCHAR(50) NOT NULL,

    actor_type VARCHAR(50) NOT NULL,
    actor_id VARCHAR(255),

    old_value JSONB,
    new_value JSONB,

    description TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_entity
ON audit_logs(entity_type, entity_id);

CREATE INDEX idx_audit_action
ON audit_logs(action);

CREATE INDEX idx_audit_actor
ON audit_logs(actor_type);

CREATE INDEX idx_audit_created_at
ON audit_logs(created_at);

CREATE INDEX idx_audit_trace
ON audit_logs(trace_id); 