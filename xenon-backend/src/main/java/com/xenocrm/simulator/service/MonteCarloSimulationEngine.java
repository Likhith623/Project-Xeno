package com.xenocrm.simulator.service;

import com.xenocrm.simulator.entity.SimulationRunEntity;
import com.xenocrm.simulator.enums.SimulationRunStatus;
import com.xenocrm.simulator.repository.SimulationRunRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * MonteCarloSimulationEngine — Runs asynchronous Monte Carlo simulations.
 * Layer: Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MonteCarloSimulationEngine {

    private final SimulationRunRepository simulationRunRepository;

    @Async("taskExecutor")
    public void runSimulationAsync(SimulationRunEntity run) {
        log.info("Starting simulation run {}", run.getId());
        try {
            // Mock simulation logic
            Thread.sleep(2000); 

            run.setPredictedOpenRate(BigDecimal.valueOf(0.42));
            run.setPredictedCtr(BigDecimal.valueOf(0.15));
            run.setPredictedConversionRate(BigDecimal.valueOf(0.05));
            run.setPredictedRevenue(BigDecimal.valueOf(15000.00));
            run.setConfidenceIntervalLow(BigDecimal.valueOf(0.38));
            run.setConfidenceIntervalHigh(BigDecimal.valueOf(0.46));
            run.setStatus(SimulationRunStatus.COMPLETED);
            run.setCompletedAt(OffsetDateTime.now());

            simulationRunRepository.save(run);
            log.info("Simulation run {} completed successfully", run.getId());
        } catch (Exception e) {
            log.error("Simulation run {} failed: {}", run.getId(), e.getMessage());
            run.setStatus(SimulationRunStatus.FAILED);
            run.setCompletedAt(OffsetDateTime.now());
            simulationRunRepository.save(run);
        }
    }
}
