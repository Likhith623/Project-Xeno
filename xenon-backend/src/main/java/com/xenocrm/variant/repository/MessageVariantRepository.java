package com.xenocrm.variant.repository;

import com.xenocrm.variant.entity.MessageVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageVariantRepository extends JpaRepository<MessageVariantEntity, UUID> {
    java.util.List<MessageVariantEntity> findAllByCampaignId(UUID campaignId);
    java.util.List<MessageVariantEntity> findAllByCampaignIdAndMabIsActiveTrue(UUID campaignId);

    @org.springframework.data.jpa.repository.Modifying @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("UPDATE MessageVariantEntity v SET v.mabAlpha = v.mabAlpha + 1 WHERE v.id = :id")
    void incrementMabAlpha(@org.springframework.data.repository.query.Param("id") UUID variantId);

    @org.springframework.data.jpa.repository.Modifying @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("UPDATE MessageVariantEntity v SET v.mabBeta = v.mabBeta + 1 WHERE v.id = :id")
    void incrementMabBeta(@org.springframework.data.repository.query.Param("id") UUID variantId);

    @org.springframework.data.jpa.repository.Modifying @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("UPDATE MessageVariantEntity v SET v.mabImpressions = v.mabImpressions + 1 WHERE v.id = :id")
    void incrementMabImpressions(@org.springframework.data.repository.query.Param("id") UUID variantId);

    @org.springframework.data.jpa.repository.Modifying @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("UPDATE MessageVariantEntity v SET v.mabConversions = v.mabConversions + 1 WHERE v.id = :id")
    void incrementMabConversions(@org.springframework.data.repository.query.Param("id") UUID variantId);

    /** Queries v_variant_mab_stats view for Thompson Sampling statistics. */
    @org.springframework.data.jpa.repository.Query(value="SELECT * FROM v_variant_mab_stats WHERE campaign_id = CAST(:campaignId AS uuid)", nativeQuery=true)
    java.util.List<Object[]> findMabStatsByCampaignId(@org.springframework.data.repository.query.Param("campaignId") String campaignId);
}
