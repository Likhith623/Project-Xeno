package com.xenocrm.order.entity;

import com.xenocrm.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * OrderItemEntity -- JPA entity mapping to the `order_items` table.
 * Layer: Domain Entity
 * Purpose: One row per line item inside an order.
 */
@Entity
@Table(name = "order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;                  // FK to orders.id NOT NULL ON DELETE CASCADE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;              // FK to products.id -- nullable

    @Column(name = "product_sku")
    private String productSku;                  // denormalised for resilience -- nullable

    @Column(name = "product_name", nullable = false)
    private String productName;                 // NOT NULL

    @Column(name = "quantity", nullable = false)
    private int quantity;                       // CHECK > 0

    @Column(name = "unit_price", precision = 12, scale = 2, nullable = false)
    private BigDecimal unitPrice;               // NUMERIC(12,2) CHECK >= 0

    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount;          // DEFAULT 0

    @Column(name = "line_total", insertable = false, updatable = false)
    private BigDecimal lineTotal;               // GENERATED ALWAYS AS STORED: (unit_price - discount_amount)*quantity
}
