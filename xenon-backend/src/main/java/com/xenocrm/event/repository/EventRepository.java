package com.xenocrm.event.repository;

import com.xenocrm.event.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * EventRepository — Spring Data JPA repository for events.
 */
@Repository
public interface EventRepository extends JpaRepository<EventEntity, UUID> {
    List<EventEntity> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);
}
