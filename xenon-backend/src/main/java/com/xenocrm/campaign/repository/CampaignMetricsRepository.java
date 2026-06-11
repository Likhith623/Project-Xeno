package com.xenocrm.campaign.repository;

import com.xenocrm.campaign.entity.CampaignMetricsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * CampaignMetricsRepository — Spring Data JPA repository for campaign metrics.
 */
@Repository
public interface CampaignMetricsRepository extends JpaRepository<CampaignMetricsEntity, UUID> {
}
