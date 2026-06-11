package com.xenocrm.order.dto;

import com.xenocrm.order.enums.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class OrderResponseDto {
    private UUID id;
    private UUID customerId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
