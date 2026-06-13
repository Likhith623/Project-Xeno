package com.xenocrm.product.entity;

import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * ProductEntity -- JPA entity mapping to the `products` table.
 * Layer: Domain Entity
 * Purpose: SKU-level catalog record with price, category, tags and attributes.
 */
@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "sku", unique = true, nullable = false)
    private String sku;                         // UNIQUE NOT NULL

    @Column(name = "name", nullable = false)
    private String name;                        // NOT NULL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProductCategoryEntity category;     // FK to product_categories.id -- nullable

    @Column(name = "price", precision = 12, scale = 2, nullable = false)
    private BigDecimal price;                   // NUMERIC(12,2) NOT NULL CHECK >= 0

    @Column(name = "currency", nullable = false)
    private String currency;                    // DEFAULT INR

    @Column(name = "brand")
    private String brand;                       // nullable

    @Type(StringArrayType.class)
    @Column(name = "tags", columnDefinition = "text[]")
    private String[] tags;                      // TEXT[] DEFAULT {}

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", columnDefinition = "jsonb")
    private Map<String, Object> attributes;     // JSONB -- color, size, etc.

    @Column(name = "is_active", nullable = false)
    private boolean isActive;                   // DEFAULT TRUE

    @Column(name = "inventory_count", nullable = false)
    private int inventoryCount;                 // DEFAULT 0

    @Column(name = "last_restocked_at")
    private OffsetDateTime lastRestockedAt;     // nullable

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
