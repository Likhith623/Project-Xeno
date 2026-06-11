package com.xenocrm.agent.repository;

import com.xenocrm.agent.entity.AgentSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AgentSessionRepository extends JpaRepository<AgentSessionEntity, UUID> {
    java.util.List<AgentSessionEntity> findAllByStatus(com.xenocrm.agent.enums.AgentSessionStatus status);
}
