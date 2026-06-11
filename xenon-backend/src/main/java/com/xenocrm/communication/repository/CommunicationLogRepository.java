package com.xenocrm.communication.repository;

import com.xenocrm.communication.entity.CommunicationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * CommunicationLogRepository — Spring Data JPA repository for communication logs.
 */
@Repository
public interface CommunicationLogRepository extends JpaRepository<CommunicationLogEntity, UUID> {
    List<CommunicationLogEntity> findByCampaignId(UUID campaignId);
    List<CommunicationLogEntity> findByCustomerId(UUID customerId);
}
