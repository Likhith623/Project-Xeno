package com.xenocrm.variant.service;

import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.exception.ResourceNotFoundException;
import com.xenocrm.variant.dto.VariantCreateRequestDto;
import com.xenocrm.variant.dto.VariantResponseDto;
import com.xenocrm.variant.entity.VariantEntity;
import com.xenocrm.variant.mapper.VariantMapper;
import com.xenocrm.variant.repository.VariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final VariantRepository variantRepository;
    private final CampaignRepository campaignRepository;
    private final VariantMapper variantMapper;

    @Transactional
    public VariantResponseDto createVariant(VariantCreateRequestDto request) {
        log.debug("Creating new variant: {}", request.getName());

        CampaignEntity campaign = campaignRepository.findById(request.getCampaignId())
                .orElseThrow(() -> new ResourceNotFoundException("Campaign", "id", request.getCampaignId()));

        VariantEntity variant = variantMapper.toEntity(request);
        variant.setCampaign(campaign);

        if (request.getLanguage() == null) {
            variant.setLanguage("en");
        }
        if (request.getIsControl() != null) {
            variant.setControl(request.getIsControl());
        } else {
            variant.setControl(false);
        }

        variant.setAlpha(1.0);
        variant.setBeta(1.0);

        VariantEntity savedVariant = variantRepository.save(variant);
        return variantMapper.toResponseDto(savedVariant);
    }

    @Transactional(readOnly = true)
    public List<VariantResponseDto> getVariantsByCampaignId(UUID campaignId) {
        if (!campaignRepository.existsById(campaignId)) {
            throw new ResourceNotFoundException("Campaign", "id", campaignId);
        }

        return variantRepository.findByCampaignId(campaignId).stream()
                .map(variantMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
