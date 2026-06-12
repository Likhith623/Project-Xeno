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
    @Query(value = "SELECT id as id, name as name, status as status, goal as goal, scheduled_at as scheduledAt, started_at as startedAt, completed_at as completedAt, created_by_agent as createdByAgent, total_sent as totalSent, total_delivered as totalDelivered, total_failed as totalFailed, total_opened as totalOpened, total_read as totalRead, total_clicked as totalClicked, total_converted as totalConverted, revenue_attributed as revenueAttributed, delivery_rate_pct as deliveryRatePct, failure_rate_pct as failureRatePct, open_rate_pct as openRatePct, ctr_pct as ctrPct, conversion_rate_pct as conversionRatePct, opt_out_rate_pct as optOutRatePct, segment_name as segmentName, segment_size as segmentSize FROM v_campaign_performance WHERE id = CAST(:id AS uuid)", nativeQuery = true)
    Optional<CampaignPerformanceProjection> findPerformanceSummaryById(@Param("id") String campaignId);
    /** Queries v_opt_out_alerts for all currently running campaigns. */
    @Query(value = "SELECT campaign_id as campaignId, campaign_name as campaignName, opt_out_rate_threshold as optOutRateThreshold, current_opt_out_rate_pct as currentOptOutRatePct, alert_level as alertLevel FROM v_opt_out_alerts", nativeQuery = true)
    List<OptOutAlertProjection> findAllOptOutAlerts();
}
