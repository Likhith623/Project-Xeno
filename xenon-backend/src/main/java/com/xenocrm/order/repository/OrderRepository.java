package com.xenocrm.order.repository;

import com.xenocrm.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * OrderRepository — Spring Data JPA repository.
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    Optional<OrderEntity> findByOrderNumber(String orderNumber);
    List<OrderEntity> findByCustomerId(UUID customerId);
}
