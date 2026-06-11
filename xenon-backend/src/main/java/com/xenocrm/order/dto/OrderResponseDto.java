package com.xenocrm.order.dto;

import com.xenocrm.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/** OrderResponseDto -- Standard API response for an order. Layer: DTO */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OrderResponseDto {
    private UUID id;
    private UUID customerId;
    private String orderNumber;
    private OrderStatus status;
    private String channel;
    private BigDecimal totalAmount;
    private String currency;
    private BigDecimal discountAmount;
    private String couponCode;
    private OffsetDateTime placedAt;
    private OffsetDateTime deliveredAt;
    private OffsetDateTime createdAt;
}
