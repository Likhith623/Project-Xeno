package com.xenocrm.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/** ProductCreateRequestDto -- DTO for creating a new product. Layer: DTO */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductCreateRequestDto {
    @NotBlank private String sku;
    @NotBlank private String name;
    private UUID categoryId;
    @NotNull private BigDecimal price;
    private String currency;
    private String brand;
    private String[] tags;
    private Map<String, Object> attributes;
    private boolean isActive;
}
