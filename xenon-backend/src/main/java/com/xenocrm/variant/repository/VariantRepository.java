package com.xenocrm.variant.repository;

import com.xenocrm.variant.entity.MessageVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * VariantRepository — Spring Data JPA repository for campaign variants.
 */
@Repository
public interface VariantRepository extends JpaRepository<MessageVariantEntity, UUID> {
    List<MessageVariantEntity> findByCampaignId(UUID campaignId);
}
