package com.xenocrm.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductCreateRequestDto {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private BigDecimal price;
    private UUID categoryId;
}
