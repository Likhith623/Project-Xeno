package com.xenocrm.communication.repository;

import com.xenocrm.communication.entity.CommunicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * CommunicationRepository — Spring Data JPA repository for communication logs.
 */
@Repository
public interface CommunicationRepository extends JpaRepository<CommunicationEntity, UUID> {
    List<CommunicationEntity> findByCampaignId(UUID campaignId);
    List<CommunicationEntity> findByCustomerId(UUID customerId);
}
