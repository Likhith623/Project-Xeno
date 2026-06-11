package com.xenocrm.order.dto;

import com.xenocrm.order.enums.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * OrderCreateRequestDto — DTO for creating a new order.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestDto {

    @NotBlank
    private String externalId;

    @NotNull
    private UUID customerId;

    @NotNull
    private BigDecimal totalAmount;

    private String currency; // Default INR

    private OrderStatus status; // Default COMPLETED
    
    private OffsetDateTime orderDate; // Default now

    private String storeId;

    private String channel;

    @NotEmpty
    @Valid
    private List<OrderItemRequestDto> items;
}
