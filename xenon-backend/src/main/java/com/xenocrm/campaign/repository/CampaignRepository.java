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
}
