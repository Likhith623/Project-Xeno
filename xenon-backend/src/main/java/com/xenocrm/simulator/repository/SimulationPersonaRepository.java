package com.xenocrm.simulator.repository;

import com.xenocrm.simulator.entity.SimulationPersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SimulationPersonaRepository extends JpaRepository<SimulationPersonaEntity, UUID> {
}
