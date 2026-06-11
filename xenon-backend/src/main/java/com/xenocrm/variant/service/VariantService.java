package com.xenocrm.variant.service;

import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.exception.ResourceNotFoundException;
import com.xenocrm.variant.dto.MessageVariantCreateRequestDto;
import com.xenocrm.variant.dto.MessageVariantResponseDto;
import com.xenocrm.variant.entity.MessageVariantEntity;
import com.xenocrm.variant.mapper.MessageVariantMapper;
import com.xenocrm.variant.repository.MessageVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * VariantService — Handles creation and retrieval of campaign variants.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VariantService {

    private final MessageVariantRepository variantRepository;
    private final CampaignRepository campaignRepository;
    private final MessageVariantMapper variantMapper;

    @Transactional
    public MessageVariantResponseDto createVariant(MessageVariantCreateRequestDto request) {
        log.debug("Creating new variant: {}", request.getName());

        CampaignEntity campaign = campaignRepository.findById(request.getCampaignId())
                .orElseThrow(() -> new ResourceNotFoundException("Campaign", "id", request.getCampaignId()));

        MessageVariantEntity variant = variantMapper.toEntity(request);
        variant.setCampaign(campaign);

        variant.setMabAlpha(BigDecimal.ONE);
        variant.setMabBeta(BigDecimal.ONE);
        variant.setMabIsActive(true);

        MessageVariantEntity savedVariant = variantRepository.save(variant);
        return variantMapper.toResponseDto(savedVariant);
    }

    @Transactional(readOnly = true)
    public List<MessageVariantResponseDto> getVariantsByCampaignId(UUID campaignId) {
        if (!campaignRepository.existsById(campaignId)) {
            throw new ResourceNotFoundException("Campaign", "id", campaignId);
        }

        return variantRepository.findAllByCampaignId(campaignId).stream()
                .map(variantMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
