package com.xenocrm.order.dto;

import com.xenocrm.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * OrderResponseDto — Standard DTO representing an order.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private UUID id;
    private String externalId;
    private UUID customerId;
    private BigDecimal totalAmount;
    private String currency;
    private OrderStatus status;
    private OffsetDateTime orderDate;
    private String storeId;
    private String channel;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<OrderItemResponseDto> items;
}
