package com.xenocrm.correction.repository;

import com.xenocrm.correction.entity.CorrectionEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * CorrectionEventRepository — Repository for the `correction_events` table.
 */
@Repository
public interface CorrectionEventRepository extends JpaRepository<CorrectionEventEntity, UUID> {
    org.springframework.data.domain.Page<CorrectionEventEntity> findByCampaignId(UUID campaignId, org.springframework.data.domain.Pageable pageable);
}
