package com.xenocrm.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/** ProductResponseDto -- Standard API response for a product. Layer: DTO */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductResponseDto {
    private UUID id;
    private String sku;
    private String name;
    private UUID categoryId;
    private String categoryName;
    private BigDecimal price;
    private String currency;
    private String brand;
    private String[] tags;
    private Map<String, Object> attributes;
    private boolean isActive;
    private OffsetDateTime createdAt;
}
