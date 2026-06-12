package com.xenocrm.variant.repository;

import com.xenocrm.variant.entity.MessageVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * MessageVariantRepository -- JPA repository for MessageVariantEntity.
 * Layer: Repository
 */
public interface MessageVariantRepository extends JpaRepository<MessageVariantEntity, UUID> {
    /** Returns all variants for a campaign. */
    List<MessageVariantEntity> findAllByCampaignId(UUID campaignId);
    /** Returns only active variants for a campaign (for MAB selection). */
    List<MessageVariantEntity> findAllByCampaignIdAndMabIsActiveTrue(UUID campaignId);
    /** Atomically increments mab_alpha (success). */
    @Modifying @Transactional
    @Query("UPDATE MessageVariantEntity v SET v.mabAlpha = v.mabAlpha + 1 WHERE v.id = :id")
    void incrementMabAlpha(@Param("id") UUID variantId);
    /** Atomically increments mab_beta (non-success). */
    @Modifying @Transactional
    @Query("UPDATE MessageVariantEntity v SET v.mabBeta = v.mabBeta + 1 WHERE v.id = :id")
    void incrementMabBeta(@Param("id") UUID variantId);
    /** Atomically increments mab_impressions (delivery count). */
    @Modifying @Transactional
    @Query("UPDATE MessageVariantEntity v SET v.mabImpressions = v.mabImpressions + 1 WHERE v.id = :id")
    void incrementMabImpressions(@Param("id") UUID variantId);
    /** Atomically increments mab_conversions (click/conversion count). */
    @Modifying @Transactional
    @Query("UPDATE MessageVariantEntity v SET v.mabConversions = v.mabConversions + 1 WHERE v.id = :id")
    void incrementMabConversions(@Param("id") UUID variantId);
    /** Queries v_variant_mab_stats view for Thompson Sampling statistics. */
    @Query(value = "SELECT id as variantId, campaign_id as campaignId, name as variantName, channel as channel, mab_alpha as mabAlpha, mab_beta as mabBeta, mab_impressions as mabImpressions, mab_conversions as mabConversions, expected_conversion_rate as expectedConversionRate, ci_half_width_95 as ciHalfWidth95, mab_is_active as mabIsActive, campaign_name as campaignName FROM v_variant_mab_stats WHERE campaign_id = CAST(:campaignId AS uuid)", nativeQuery = true)
    List<MabStatsProjection> findMabStatsByCampaignId(@Param("campaignId") String campaignId);
}
