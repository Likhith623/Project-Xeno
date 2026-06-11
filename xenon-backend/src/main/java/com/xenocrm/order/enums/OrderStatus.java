package com.xenocrm.order.enums;

/**
 * OrderStatus -- Domain enum for status IN orders.
 * Layer: Domain Enum
 * Converter: OrderStatusConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum OrderStatus {
    PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED, RETURNED
}
