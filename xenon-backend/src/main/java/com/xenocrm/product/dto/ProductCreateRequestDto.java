package com.xenocrm.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * ProductCreateRequestDto — DTO for creating a new product.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequestDto {

    private String externalId;

    @NotBlank
    private String name;

    private String description;
    
    private String sku;

    @NotNull
    private BigDecimal price;

    private String currency; // Default INR handled in service/entity if null

    private String imageUrl;

    private String[] tags;

    private UUID categoryId;
    
    private Boolean isActive;
}
