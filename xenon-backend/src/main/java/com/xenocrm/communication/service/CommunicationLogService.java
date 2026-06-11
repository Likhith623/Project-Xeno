package com.xenocrm.communication.service;

import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.communication.dto.CommunicationLogResponseDto;
import com.xenocrm.communication.entity.CommunicationLogEntity;
import com.xenocrm.communication.enums.CommunicationStatus;
import com.xenocrm.communication.mapper.CommunicationLogMapper;
import com.xenocrm.communication.repository.CommunicationLogRepository;
import com.xenocrm.customer.entity.CustomerEntity;
import com.xenocrm.customer.repository.CustomerRepository;
import com.xenocrm.exception.ResourceNotFoundException;
import com.xenocrm.variant.entity.VariantEntity;
import com.xenocrm.variant.repository.VariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * CommunicationLogService — Handles tracking of communication logs.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommunicationLogService {

    private final CommunicationLogRepository communicationLogRepository;
    private final CustomerRepository customerRepository;
    private final CampaignRepository campaignRepository;
    private final VariantRepository variantRepository;
    private final CommunicationLogMapper communicationLogMapper;

    @Transactional
    public CommunicationLogResponseDto logCommunication(UUID customerId, UUID campaignId, UUID variantId, String channel) {
        log.debug("Logging communication for customer: {}, campaign: {}", customerId, campaignId);

        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));

        CampaignEntity campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign", "id", campaignId));

        VariantEntity variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Variant", "id", variantId));

        CommunicationLogEntity logEntity = new CommunicationLogEntity();
        logEntity.setCustomer(customer);
        logEntity.setCampaign(campaign);
        logEntity.setVariant(variant);
        logEntity.setChannel(channel);
        logEntity.setStatus(CommunicationStatus.PENDING);

        CommunicationLogEntity savedLog = communicationLogRepository.save(logEntity);
        return communicationLogMapper.toResponseDto(savedLog);
    }

    @Transactional
    public void updateStatus(UUID id, CommunicationStatus status) {
        log.debug("Updating status for communication log {} to {}", id, status);

        CommunicationLogEntity logEntity = communicationLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CommunicationLog", "id", id));

        logEntity.setStatus(status);

        OffsetDateTime now = OffsetDateTime.now();
        switch (status) {
            case SENT:
                logEntity.setSentAt(now);
                break;
            case DELIVERED:
                logEntity.setDeliveredAt(now);
                break;
            case READ:
                logEntity.setOpenedAt(now);
                break;
            default:
                break;
        }

        communicationLogRepository.save(logEntity);
    }

    @Transactional(readOnly = true)
    public List<CommunicationLogResponseDto> getLogsByCampaign(UUID campaignId) {
        return communicationLogRepository.findByCampaignId(campaignId).stream()
                .map(communicationLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommunicationLogResponseDto> getLogsByCustomer(UUID customerId) {
        return communicationLogRepository.findByCustomerId(customerId).stream()
                .map(communicationLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
