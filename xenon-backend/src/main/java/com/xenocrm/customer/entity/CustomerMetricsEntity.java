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
    private UUID customerId; // PRIMARY KEY = customers.id — uses @MapsId

    @Column(name = "total_orders")
    private Integer totalOrders;

    @Column(name = "total_spent", precision = 12, scale = 2)
    private BigDecimal totalSpent;

    @Column(name = "last_order_date")
    private OffsetDateTime lastOrderDate;

    @Column(name = "aov", precision = 12, scale = 2)
    private BigDecimal aov;

    @Column(name = "purchase_frequency", precision = 10, scale = 2)
    private BigDecimal purchaseFrequency;

    @Column(name = "churn_probability", precision = 5, scale = 4)
    private BigDecimal churnProbability;

    @Column(name = "predicted_ltv", precision = 12, scale = 2)
    private BigDecimal predictedLtv;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;
}
