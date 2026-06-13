package com.xenocrm.campaign.service;

import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.enums.CampaignStatus;
import com.xenocrm.campaign.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * Phase 7: Autonomous Budget Re-Allocation (The Fund Manager)
 * Calculates real-time ROAS of all RUNNING campaigns.
 * Drains budget from losers and reinvests in winners.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignFundManagerJob {

    private final CampaignRepository campaignRepository;

    @Scheduled(cron = "0 0 * * * ?") // Runs hourly
    public void optimizeBudgets() {
        log.info("💰 Running Autonomous Fund Manager Job...");

        List<CampaignEntity> runningCampaigns = campaignRepository.findAllByStatus(CampaignStatus.RUNNING);

        if (runningCampaigns.size() < 2) {
            log.info("Not enough running campaigns to perform arbitrage.");
            return;
        }

        CampaignEntity highestRoasCampaign = null;
        CampaignEntity lowestRoasCampaign = null;
        BigDecimal highestRoas = BigDecimal.ZERO;
        // Start lowest at an impossibly high number
        BigDecimal lowestRoas = new BigDecimal("999999.00");

        for (CampaignEntity c : runningCampaigns) {
            BigDecimal spend = c.getCurrentSpend() != null ? c.getCurrentSpend() : BigDecimal.ZERO;
            BigDecimal revenue = c.getRevenueAttributed() != null ? c.getRevenueAttributed() : BigDecimal.ZERO;
            BigDecimal allocated = c.getBudgetAllocated() != null ? c.getBudgetAllocated() : BigDecimal.ZERO;

            if (spend.compareTo(BigDecimal.ZERO) == 0 || allocated.compareTo(BigDecimal.ZERO) == 0) {
                continue; // Cannot calculate ROAS or drain budget if none is spent/allocated
            }

            BigDecimal roas = revenue.divide(spend, 4, RoundingMode.HALF_UP);

            if (roas.compareTo(highestRoas) > 0) {
                highestRoas = roas;
                highestRoasCampaign = c;
            }
            if (roas.compareTo(lowestRoas) < 0) {
                lowestRoas = roas;
                lowestRoasCampaign = c;
            }
        }

        // If we found a clear loser and a clear winner, and they are different
        if (highestRoasCampaign != null && lowestRoasCampaign != null &&
                !highestRoasCampaign.getId().equals(lowestRoasCampaign.getId())) {

            if (lowestRoas.compareTo(new BigDecimal("1.0")) < 0) { // Bleeding money (ROAS < 1)
                
                BigDecimal lowestAllocated = lowestRoasCampaign.getBudgetAllocated();
                BigDecimal lowestSpend = lowestRoasCampaign.getCurrentSpend();
                BigDecimal remainingBudget = lowestAllocated.subtract(lowestSpend);

                if (remainingBudget.compareTo(BigDecimal.ZERO) > 0) {
                    // Drain 50% of the remaining budget from the loser
                    BigDecimal transferAmount = remainingBudget.multiply(new BigDecimal("0.50")).setScale(2, RoundingMode.HALF_UP);
                    
                    if (transferAmount.compareTo(BigDecimal.ZERO) > 0) {
                        log.info("📉 Detected bleeding campaign: {} (ROAS: {}). Draining ${}", lowestRoasCampaign.getName(), lowestRoas, transferAmount);
                        log.info("📈 Re-investing into winning campaign: {} (ROAS: {})", highestRoasCampaign.getName(), highestRoas);

                        // Perform Transfer
                        lowestRoasCampaign.setBudgetAllocated(lowestAllocated.subtract(transferAmount));
                        highestRoasCampaign.setBudgetAllocated(
                                (highestRoasCampaign.getBudgetAllocated() != null ? highestRoasCampaign.getBudgetAllocated() : BigDecimal.ZERO)
                                        .add(transferAmount)
                        );

                        campaignRepository.saveAll(List.of(lowestRoasCampaign, highestRoasCampaign));
                        log.info("💸 Fund Transfer Complete. Autonomous Optimization Successful.");
                    }
                }
            } else {
                log.info("All active campaigns are profitable. No arbitrage executed.");
            }
        } else {
            log.info("No viable arbitrage opportunities detected.");
        }
    }
}
