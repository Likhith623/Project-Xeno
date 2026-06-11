package com.xenocrm.simulator.service;

import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.simulator.dto.SimulationRunRequestDto;
import com.xenocrm.simulator.entity.SimulationRunEntity;
import com.xenocrm.simulator.enums.SimulationRunStatus;
import com.xenocrm.simulator.repository.SimulationRunRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

/**
 * AudienceSimulationOrchestrationService — Orchestrates audience simulation runs.
 * Layer: Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AudienceSimulationOrchestrationService {

    private final SimulationRunRepository simulationRunRepository;
    private final CampaignRepository campaignRepository;
    private final MonteCarloSimulationEngine monteCarloSimulationEngine;

    public SimulationRunEntity triggerSimulation(SimulationRunRequestDto requestDto) {
        CampaignEntity campaign = campaignRepository.findById(requestDto.getCampaignId())
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found"));

        SimulationRunEntity run = SimulationRunEntity.builder()
                .campaign(campaign)
                .status(SimulationRunStatus.RUNNING)
                .syntheticAudienceSize(requestDto.getSyntheticAudienceSize())
                .personaDistribution(requestDto.getPersonaDistribution())
                .startedAt(OffsetDateTime.now())
                .build();

        run = simulationRunRepository.save(run);
        
        monteCarloSimulationEngine.runSimulationAsync(run);

        return run;
    }
}
