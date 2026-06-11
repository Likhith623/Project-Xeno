package com.xenocrm.communication.repository;

import com.xenocrm.communication.entity.CommunicationEntity;
import com.xenocrm.communication.enums.CommunicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * CommunicationRepository -- JPA repository for CommunicationEntity.
 * Layer: Repository
 * Contains all query methods needed to manage the delivery state machine.
 */
public interface CommunicationRepository extends JpaRepository<CommunicationEntity, UUID> {

    /** Returns all communications for a campaign, paginated. */
    Page<CommunicationEntity> findAllByCampaignId(UUID campaignId, Pageable pageable);

    /** Returns all communications sent to a specific customer, paginated. */
    Page<CommunicationEntity> findAllByCustomerId(UUID customerId, Pageable pageable);

    /** Finds a communication by the external channel message ID (used for callback matching). */
    Optional<CommunicationEntity> findByChannelMessageId(String channelMessageId);

    /** Counts communications for a campaign with a specific delivery status. */
    long countByCampaignIdAndStatus(UUID campaignId, CommunicationStatus status);

    /** Counts all unsubscribed communications for a campaign (used for opt-out rate calculation). */
    @Query("SELECT COUNT(c) FROM CommunicationEntity c WHERE c.campaign.id = :campaignId AND c.status = com.xenocrm.communication.enums.CommunicationStatus.UNSUBSCRIBED")
    long countUnsubscribedByCampaignId(@Param("campaignId") UUID campaignId);
}
