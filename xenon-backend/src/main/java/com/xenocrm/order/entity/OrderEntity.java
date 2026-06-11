package com.xenocrm.order.entity;

import com.xenocrm.customer.entity.CustomerEntity;
import com.xenocrm.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * OrderEntity -- JPA entity mapping to the `orders` table.
 * Layer: Domain Entity
 * Purpose: One row per order header; line items in order_items.
 * Relationships: ManyToOne customer; OneToMany orderItems.
 */
@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;            // FK to customers.id NOT NULL ON DELETE RESTRICT

    @Column(name = "order_number", unique = true)
    private String orderNumber;                 // UNIQUE, nullable

    
    @Column(name = "status")
    private OrderStatus status;                 // pending/confirmed/shipped/delivered/cancelled/returned

    @Column(name = "channel")
    private String channel;                     // web,app,store,whatsapp -- nullable

    @Column(name = "total_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalAmount;             // NUMERIC(12,2) NOT NULL CHECK >= 0

    @Column(name = "currency", nullable = false)
    private String currency;                    // DEFAULT INR

    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount;          // DEFAULT 0

    @Column(name = "coupon_code")
    private String couponCode;                  // nullable

    @Builder.Default
    @Column(name = "placed_at", nullable = false)
    private OffsetDateTime placedAt = OffsetDateTime.now();            // NOT NULL DEFAULT NOW()

    @Column(name = "delivered_at")
    private OffsetDateTime deliveredAt;         // nullable

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, Object> metadata;       // JSONB DEFAULT {}

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    /** Convenience method to add a line item and set the back-reference. */
    public void addItem(OrderItemEntity item) {
        orderItems.add(item);
        item.setOrder(this);
    }
}
