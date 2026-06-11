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
    org.springframework.data.domain.Page<CommunicationEntity> findAllByCampaignId(UUID campaignId, org.springframework.data.domain.Pageable pageable);
    org.springframework.data.domain.Page<CommunicationEntity> findAllByCustomerId(UUID customerId, org.springframework.data.domain.Pageable pageable);
    java.util.Optional<CommunicationEntity> findByChannelMessageId(String channelMessageId);
    long countByCampaignIdAndStatus(UUID campaignId, com.xenocrm.communication.enums.CommunicationStatus status);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(c) FROM CommunicationEntity c WHERE c.campaign.id = :campaignId AND c.status = com.xenocrm.communication.enums.CommunicationStatus.UNSUBSCRIBED")
    long countUnsubscribedByCampaignId(@org.springframework.data.repository.query.Param("campaignId") UUID campaignId);
}
