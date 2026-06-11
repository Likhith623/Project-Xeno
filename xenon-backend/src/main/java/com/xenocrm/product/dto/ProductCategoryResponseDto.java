package com.xenocrm.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * ProductCategoryResponseDto — Standard DTO representing a product category.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryResponseDto {
    private UUID id;
    private String externalId;
    private String name;
    private String description;
    private UUID parentCategoryId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
