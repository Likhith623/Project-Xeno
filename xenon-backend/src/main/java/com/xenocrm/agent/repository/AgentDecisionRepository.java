package com.xenocrm.agent.repository;

import com.xenocrm.agent.entity.AgentDecisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

/**
 * AgentDecisionRepository -- JPA repository for AgentDecisionEntity.
 * Layer: Repository
 */
public interface AgentDecisionRepository extends JpaRepository<AgentDecisionEntity, UUID> {
    /** Finds all decisions for a session in chronological step order. */
    List<AgentDecisionEntity> findAllBySessionIdOrderByStepNumberAsc(UUID sessionId);
}
