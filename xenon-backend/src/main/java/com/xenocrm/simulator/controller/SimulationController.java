package com.xenocrm.simulator.controller;

import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.simulator.dto.SimulationRunRequestDto;
import com.xenocrm.simulator.dto.SimulationRunResultDto;
import com.xenocrm.simulator.entity.SimulationRunEntity;
import com.xenocrm.simulator.mapper.SimulationMapper;
import com.xenocrm.simulator.repository.SimulationRunRepository;
import com.xenocrm.simulator.service.AudienceSimulationOrchestrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * SimulationController — API endpoints for audience simulation.
 */
@RestController
@RequestMapping("/api/v1/simulations")
@RequiredArgsConstructor
@Tag(name = "Audience Simulator", description = "Endpoints for running and viewing Monte Carlo campaign simulations")
public class SimulationController {

    private final AudienceSimulationOrchestrationService orchestrationService;
    private final SimulationRunRepository simulationRunRepository;
    private final SimulationMapper simulationMapper;

    @PostMapping
    @Operation(summary = "Trigger a new audience simulation")
    public ResponseEntity<ResponseWrapper<SimulationRunResultDto>> triggerSimulation(@RequestBody SimulationRunRequestDto requestDto) {
        SimulationRunEntity run = orchestrationService.triggerSimulation(requestDto);
        return ResponseEntity.ok(ResponseWrapper.success(simulationMapper.toResultDto(run), "Simulation started successfully"));
    }

    @PostMapping("/campaigns/{id}/simulate")
    @Operation(summary = "Trigger a simulation for a specific campaign")
    public ResponseEntity<ResponseWrapper<SimulationRunResultDto>> triggerSimulationForCampaign(@PathVariable UUID id, @RequestBody SimulationRunRequestDto requestDto) {
        requestDto.setCampaignId(id);
        SimulationRunEntity run = orchestrationService.triggerSimulation(requestDto);
        return ResponseEntity.ok(ResponseWrapper.success(simulationMapper.toResultDto(run), "Simulation started successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get simulation run result")
    public ResponseEntity<ResponseWrapper<SimulationRunResultDto>> getSimulationRun(@PathVariable UUID id) {
        SimulationRunResultDto resultDto = orchestrationService.getSimulationRun(id);
        return ResponseEntity.ok(ResponseWrapper.success(resultDto, "Retrieved simulation run"));
    }

    @PostMapping("/campaigns/{id}/counterfactual")
    @Operation(summary = "Run a counterfactual simulation for a campaign")
    public ResponseEntity<ResponseWrapper<com.xenocrm.simulator.dto.CounterfactualResultDto>> runCounterfactual(
            @PathVariable UUID id, 
            @RequestParam String channel, 
            @org.springframework.beans.factory.annotation.Autowired com.xenocrm.simulator.service.CounterfactualSimulationService counterfactualService) {
        com.xenocrm.simulator.dto.CounterfactualResultDto result = counterfactualService.runCounterfactual(id, channel);
        return ResponseEntity.ok(ResponseWrapper.success(result, "Counterfactual simulation completed"));
    }
}
