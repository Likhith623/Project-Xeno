package com.xenocrm.order.dto;

import com.xenocrm.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** OrderCreateRequestDto -- DTO for ingesting a new order. Layer: DTO */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OrderCreateRequestDto {
    @NotNull private UUID customerId;
    private String orderNumber;
    private OrderStatus status;
    private String channel;
    @NotNull private BigDecimal totalAmount;
    private String currency;
    private BigDecimal discountAmount;
    private String couponCode;
    private OffsetDateTime placedAt;
    private Map<String, Object> metadata;
    private List<OrderItemDto> items;
}
