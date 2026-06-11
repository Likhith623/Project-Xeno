package com.xenocrm.variant.service;

import com.xenocrm.variant.entity.VariantEntity;
import com.xenocrm.variant.repository.VariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * MultiArmedBanditService — Implements Thompson Sampling for variant selection.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MultiArmedBanditService {

    private final VariantRepository variantRepository;

    /**
     * Selects the best variant for a campaign using Thompson Sampling.
     */
    @Transactional(readOnly = true)
    public VariantEntity selectBestVariant(UUID campaignId) {
        List<VariantEntity> variants = variantRepository.findByCampaignId(campaignId);
        
        if (variants.isEmpty()) {
            throw new IllegalStateException("No variants found for campaign " + campaignId);
        }
        
        if (variants.size() == 1) {
            return variants.get(0);
        }

        VariantEntity bestVariant = null;
        double maxSample = -1.0;

        for (VariantEntity variant : variants) {
            // Thompson sampling uses Beta distribution
            BetaDistribution betaDistribution = new BetaDistribution(variant.getAlpha(), variant.getBeta());
            double sample = betaDistribution.sample();
            
            log.trace("Variant {} sampled value: {}", variant.getId(), sample);
            
            if (sample > maxSample) {
                maxSample = sample;
                bestVariant = variant;
            }
        }

        return bestVariant;
    }

    /**
     * Updates alpha (success) or beta (failure) for a variant.
     */
    @Transactional
    public void recordFeedback(UUID variantId, boolean success) {
        variantRepository.findById(variantId).ifPresent(variant -> {
            if (success) {
                variant.setAlpha(variant.getAlpha() + 1.0);
            } else {
                variant.setBeta(variant.getBeta() + 1.0);
            }
            variantRepository.save(variant);
        });
    }
}
