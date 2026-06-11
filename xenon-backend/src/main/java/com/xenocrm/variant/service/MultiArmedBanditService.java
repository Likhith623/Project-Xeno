package com.xenocrm.variant.service;

import com.xenocrm.variant.entity.MessageVariantEntity;
import com.xenocrm.variant.repository.MessageVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

/**
 * MultiArmedBanditService — Implements Thompson Sampling for variant selection.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MultiArmedBanditService {

    private final MessageVariantRepository variantRepository;

    /**
     * Selects the best variant for a campaign using Thompson Sampling.
     */
    @Transactional(readOnly = true)
    public MessageVariantEntity selectBestVariant(UUID campaignId) {
        List<MessageVariantEntity> variants = variantRepository.findAllByCampaignId(campaignId);
        
        if (variants.isEmpty()) {
            throw new IllegalStateException("No variants found for campaign " + campaignId);
        }
        
        if (variants.size() == 1) {
            return variants.get(0);
        }

        MessageVariantEntity bestVariant = null;
        double maxSample = -1.0;

        for (MessageVariantEntity variant : variants) {
            // Thompson sampling uses Beta distribution
            double alpha = variant.getMabAlpha() != null ? variant.getMabAlpha().doubleValue() : 1.0;
            double beta = variant.getMabBeta() != null ? variant.getMabBeta().doubleValue() : 1.0;
            BetaDistribution betaDistribution = new BetaDistribution(alpha, beta);
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
                variantRepository.incrementMabAlpha(variantId);
            } else {
                variantRepository.incrementMabBeta(variantId);
            }
        });
    }
}
