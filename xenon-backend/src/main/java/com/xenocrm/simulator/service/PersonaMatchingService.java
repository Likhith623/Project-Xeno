package com.xenocrm.simulator.service;

import com.xenocrm.simulator.entity.SimulationPersonaEntity;
import com.xenocrm.simulator.repository.SimulationPersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PersonaMatchingService — Maps dynamic segment members to simulation personas.
 * Layer: Service
 */
@Service
@RequiredArgsConstructor
public class PersonaMatchingService {

    private final SimulationPersonaRepository personaRepository;

    public List<SimulationPersonaEntity> getAllPersonas() {
        return personaRepository.findAll();
    }
}
