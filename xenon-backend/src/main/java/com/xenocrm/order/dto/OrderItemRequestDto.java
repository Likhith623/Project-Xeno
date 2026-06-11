package com.xenocrm.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * OrderItemRequestDto — DTO for an item within a new order request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {

    @NotNull
    private UUID productId;

    @NotNull
    private Integer quantity; // Default 1

    @NotNull
    private BigDecimal unitPrice;

    @NotNull
    private BigDecimal subtotal;
}
