package com.xenocrm.simulator.repository;

import com.xenocrm.simulator.entity.SimulationResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SimulationResultRepository extends JpaRepository<SimulationResultEntity, UUID> {
    List<SimulationResultEntity> findAllByRunId(UUID runId);
}
