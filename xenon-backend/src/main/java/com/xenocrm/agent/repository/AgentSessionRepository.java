package com.xenocrm.agent.repository;

import com.xenocrm.agent.entity.AgentSessionEntity;
import com.xenocrm.agent.enums.AgentSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

/**
 * AgentSessionRepository -- JPA repository for AgentSessionEntity.
 * Layer: Repository
 */
public interface AgentSessionRepository extends JpaRepository<AgentSessionEntity, UUID> {
    /** Finds all sessions with the given status (e.g. all RUNNING sessions). */
    List<AgentSessionEntity> findAllByStatus(AgentSessionStatus status);
}
