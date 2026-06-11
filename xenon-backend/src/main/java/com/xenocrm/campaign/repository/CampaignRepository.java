package com.xenocrm.campaign.repository;

import com.xenocrm.campaign.entity.CampaignEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * CampaignRepository — Spring Data JPA repository for campaigns.
 */
@Repository
public interface CampaignRepository extends JpaRepository<CampaignEntity, UUID> {
    java.util.List<CampaignEntity> findAllByStatus(com.xenocrm.campaign.enums.CampaignStatus status);
    java.util.List<CampaignEntity> findAllByCreatedByAgentTrue();

    /** Atomically increments total_sent. Use UPDATE not read-modify-write. */
    @org.springframework.data.jpa.repository.Modifying @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("UPDATE CampaignEntity c SET c.totalSent = c.totalSent + 1 WHERE c.id = :id")
    void incrementTotalSent(@org.springframework.data.repository.query.Param("id") UUID campaignId);

    /** Same pattern for each counter: */
    @org.springframework.data.jpa.repository.Modifying @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("UPDATE CampaignEntity c SET c.totalDelivered = c.totalDelivered + 1 WHERE c.id = :id")
    void incrementTotalDelivered(@org.springframework.data.repository.query.Param("id") UUID campaignId);

    @org.springframework.data.jpa.repository.Modifying @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("UPDATE CampaignEntity c SET c.totalFailed = c.totalFailed + 1 WHERE c.id = :id")
    void incrementTotalFailed(@org.springframework.data.repository.query.Param("id") UUID campaignId);

    @org.springframework.data.jpa.repository.Modifying @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("UPDATE CampaignEntity c SET c.totalOpened = c.totalOpened + 1 WHERE c.id = :id")
    void incrementTotalOpened(@org.springframework.data.repository.query.Param("id") UUID campaignId);

    @org.springframework.data.jpa.repository.Modifying @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("UPDATE CampaignEntity c SET c.totalRead = c.totalRead + 1 WHERE c.id = :id")
    void incrementTotalRead(@org.springframework.data.repository.query.Param("id") UUID campaignId);

    @org.springframework.data.jpa.repository.Modifying @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("UPDATE CampaignEntity c SET c.totalClicked = c.totalClicked + 1 WHERE c.id = :id")
    void incrementTotalClicked(@org.springframework.data.repository.query.Param("id") UUID campaignId);

    @org.springframework.data.jpa.repository.Modifying @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("UPDATE CampaignEntity c SET c.totalConverted = c.totalConverted + 1 WHERE c.id = :id")
    void incrementTotalConverted(@org.springframework.data.repository.query.Param("id") UUID campaignId);

    /** Queries v_campaign_performance view for one campaign's performance summary. */
    @org.springframework.data.jpa.repository.Query(value="SELECT * FROM v_campaign_performance WHERE id = CAST(:id AS uuid)", nativeQuery=true)
    java.util.Optional<Object[]> findPerformanceSummaryById(@org.springframework.data.repository.query.Param("id") String campaignId);

    /** Queries v_opt_out_alerts for all currently running campaigns. */
    @org.springframework.data.jpa.repository.Query(value="SELECT * FROM v_opt_out_alerts", nativeQuery=true)
    java.util.List<Object[]> findAllOptOutAlerts();
}
