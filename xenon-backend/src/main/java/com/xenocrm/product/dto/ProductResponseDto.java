package com.xenocrm.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * ProductResponseDto — Standard DTO representing a product.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private UUID id;
    private String externalId;
    private String name;
    private String description;
    private String sku;
    private BigDecimal price;
    private String currency;
    private String imageUrl;
    private String[] tags;
    private ProductCategoryResponseDto category;
    private boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
