package com.xenocrm.customer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * CustomerMetricsEntity — JPA entity mapping to the `customer_metrics` table.
 * Layer: Domain Entity
 * Purpose: Contains computed metrics (RFM, behaviour) for a customer.
 * Relationships: OneToOne mapped by CustomerEntity.
 */
@Entity
@Table(name = "customer_metrics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerMetricsEntity {

    @Id
    @Column(name = "customer_id")
    private UUID customerId;                // PRIMARY KEY = customers.id — uses @MapsId

    @Column(name = "recency_days")
    private Integer recencyDays;            // Days since most recent order — nullable until first compute

    @Column(name = "frequency")
    private Integer frequency;              // Total number of orders across all time — nullable

    @Column(name = "monetary_total", precision = 12, scale = 2)
    private BigDecimal monetaryTotal;       // Sum of all order total_amount — nullable

    @Column(name = "monetary_avg_order", precision = 12, scale = 2)
    private BigDecimal monetaryAvgOrder;    // Average order value — nullable

    @Column(name = "rfm_score", precision = 4, scale = 2)
    private BigDecimal rfmScore;            // Composite RFM score 1–5 — nullable

    @Column(name = "total_orders_last_30d")
    private Integer totalOrdersLast30d;     // Count of orders in last 30 days DEFAULT 0

    @Column(name = "total_orders_last_90d")
    private Integer totalOrdersLast90d;     // Count of orders in last 90 days DEFAULT 0

    @Column(name = "avg_days_between_orders", precision = 6, scale = 2)
    private BigDecimal avgDaysBetweenOrders; // Average days between consecutive orders — nullable

    @Column(name = "favourite_category_id")
    private UUID favouriteCategoryId;       // FK to product_categories.id — nullable

    @Column(name = "favourite_channel")
    private String favouriteChannel;        // Most used purchase channel: 'web','app','store' — nullable

    @Column(name = "clv_predicted", precision = 12, scale = 2)
    private BigDecimal clvPredicted;        // Predicted 12-month customer lifetime value — nullable

    @Column(name = "churn_probability", precision = 5, scale = 4)
    private BigDecimal churnProbability;    // Probability 0–1 of churning — nullable

    @Column(name = "email_open_rate", precision = 5, scale = 4)
    private BigDecimal emailOpenRate;       // Email open rate from last 90 days DEFAULT 0

    @Column(name = "email_click_rate", precision = 5, scale = 4)
    private BigDecimal emailClickRate;      // Email click rate from last 90 days DEFAULT 0

    @Column(name = "whatsapp_read_rate", precision = 5, scale = 4)
    private BigDecimal whatsappReadRate;    // WhatsApp read rate from last 90 days DEFAULT 0

    @Column(name = "sms_click_rate", precision = 5, scale = 4)
    private BigDecimal smsClickRate;        // SMS click rate from last 90 days DEFAULT 0

    @Column(name = "last_computed_at", nullable = false)
    private OffsetDateTime lastComputedAt;  // When these metrics were last recalculated

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;        // The customer these metrics belong to
}
