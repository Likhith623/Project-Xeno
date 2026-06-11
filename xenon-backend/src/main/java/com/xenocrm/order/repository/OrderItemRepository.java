package com.xenocrm.order.repository;

import com.xenocrm.order.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * OrderItemRepository — Spring Data JPA repository.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, UUID> {
}
