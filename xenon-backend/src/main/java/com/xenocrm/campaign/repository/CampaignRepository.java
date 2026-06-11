package com.xenocrm.campaign.repository;

import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.enums.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * CampaignRepository -- JPA repository for CampaignEntity.
 * Layer: Repository
 * Contains atomic counter-increment queries to avoid read-modify-write race conditions.
 */
public interface CampaignRepository extends JpaRepository<CampaignEntity, UUID> {
    /** Returns all campaigns with the given status. */
    List<CampaignEntity> findAllByStatus(CampaignStatus status);
    /** Returns all campaigns created by the Sovereign Agent. */
    List<CampaignEntity> findAllByCreatedByAgentTrue();
    /** Atomically increments total_sent counter. */
    @Modifying @Transactional
    @Query("UPDATE CampaignEntity c SET c.totalSent = c.totalSent + 1 WHERE c.id = :id")
    void incrementTotalSent(@Param("id") UUID campaignId);
    /** Atomically increments total_delivered counter. */
    @Modifying @Transactional
    @Query("UPDATE CampaignEntity c SET c.totalDelivered = c.totalDelivered + 1 WHERE c.id = :id")
    void incrementTotalDelivered(@Param("id") UUID campaignId);
    /** Atomically increments total_failed counter. */
    @Modifying @Transactional
    @Query("UPDATE CampaignEntity c SET c.totalFailed = c.totalFailed + 1 WHERE c.id = :id")
    void incrementTotalFailed(@Param("id") UUID campaignId);
    /** Atomically increments total_opened counter. */
    @Modifying @Transactional
    @Query("UPDATE CampaignEntity c SET c.totalOpened = c.totalOpened + 1 WHERE c.id = :id")
    void incrementTotalOpened(@Param("id") UUID campaignId);
    /** Atomically increments total_read counter. */
    @Modifying @Transactional
    @Query("UPDATE CampaignEntity c SET c.totalRead = c.totalRead + 1 WHERE c.id = :id")
    void incrementTotalRead(@Param("id") UUID campaignId);
    /** Atomically increments total_clicked counter. */
    @Modifying @Transactional
    @Query("UPDATE CampaignEntity c SET c.totalClicked = c.totalClicked + 1 WHERE c.id = :id")
    void incrementTotalClicked(@Param("id") UUID campaignId);
    /** Atomically increments total_converted counter. */
    @Modifying @Transactional
    @Query("UPDATE CampaignEntity c SET c.totalConverted = c.totalConverted + 1 WHERE c.id = :id")
    void incrementTotalConverted(@Param("id") UUID campaignId);
    /** Queries v_campaign_performance view for one campaign performance summary. */
    @Query(value = "SELECT * FROM v_campaign_performance WHERE id = CAST(:id AS uuid)", nativeQuery = true)
    Optional<Object[]> findPerformanceSummaryById(@Param("id") String campaignId);
    /** Queries v_opt_out_alerts for all currently running campaigns. */
    @Query(value = "SELECT * FROM v_opt_out_alerts", nativeQuery = true)
    List<Object[]> findAllOptOutAlerts();
}
