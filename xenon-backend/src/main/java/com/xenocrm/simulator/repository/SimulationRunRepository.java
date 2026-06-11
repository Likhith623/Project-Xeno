package com.xenocrm.simulator.repository;

import com.xenocrm.simulator.entity.SimulationRunEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SimulationRunRepository extends JpaRepository<SimulationRunEntity, UUID> {
    List<SimulationRunEntity> findAllByCampaignId(UUID campaignId);
}
