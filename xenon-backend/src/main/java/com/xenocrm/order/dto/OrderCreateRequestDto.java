package com.xenocrm.order.dto;

import com.xenocrm.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderCreateRequestDto {
    @NotNull
    private UUID customerId;
    @NotNull
    private BigDecimal totalAmount;
    private OrderStatus status;
}
