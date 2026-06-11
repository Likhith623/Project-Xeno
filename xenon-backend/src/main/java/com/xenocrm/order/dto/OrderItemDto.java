package com.xenocrm.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

/** OrderItemDto -- DTO for a line item within an order create/response. Layer: DTO */
@Data
public class OrderItemDto {
    private UUID productId;
    private String productSku;
    @NotBlank private String productName;
    @NotNull private Integer quantity;
    @NotNull private BigDecimal unitPrice;
    private BigDecimal discountAmount;
    private BigDecimal lineTotal;  // read-only GENERATED column
}
