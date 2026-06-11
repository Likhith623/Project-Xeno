package com.xenocrm.communication.service;

import com.xenocrm.communication.dto.CommunicationResponseDto;
import com.xenocrm.communication.entity.CommunicationEntity;
import com.xenocrm.communication.mapper.CommunicationMapper;
import com.xenocrm.communication.repository.CommunicationRepository;
import com.xenocrm.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * CommunicationService — Handles retrieval of communication logs.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommunicationService {

    private final CommunicationRepository communicationRepository;
    private final CommunicationMapper communicationMapper;

    @Transactional(readOnly = true)
    public List<CommunicationResponseDto> getCommunicationsByCampaignId(UUID campaignId) {
        return communicationRepository.findByCampaignId(campaignId).stream()
                .map(communicationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommunicationResponseDto> getCommunicationsByCustomerId(UUID customerId) {
        return communicationRepository.findByCustomerId(customerId).stream()
                .map(communicationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateStatus(UUID id, com.xenocrm.communication.enums.CommunicationStatus status) {
        CommunicationEntity entity = communicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Communication", "id", id));
        entity.setStatus(status);
        communicationRepository.save(entity);
    }
}
