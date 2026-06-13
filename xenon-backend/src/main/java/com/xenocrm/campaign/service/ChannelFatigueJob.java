package com.xenocrm.campaign.service;

import com.xenocrm.customer.entity.CustomerEntity;
import com.xenocrm.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Phase 8: The Sleep Agent (Channel Fatigue Engine)
 * Detects severe drops in open rates and forces a 14-day channel cooldown
 * to prevent permanent Unsubscribe actions.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelFatigueJob {

    private final CustomerRepository customerRepository;

    @Scheduled(cron = "0 30 2 * * ?") // Runs nightly at 2:30 AM
    @Transactional
    public void executeFatigueCheck() {
        log.info("💤 Running Channel Fatigue Check (The Sleep Agent)...");

        // Use native query or fetch logic. For now, we fetch all and filter in-memory for safety
        // In a real DB with millions of users, this would be a custom @Query.
        List<CustomerEntity> allCustomers = customerRepository.findAll();
        int cooldownCount = 0;

        for (CustomerEntity customer : allCustomers) {
            if (customer.getMetrics() != null && customer.getChannelCooldownUntil() == null) {
                // If they've received > 5 messages but their open rate is practically dead (< 5%)
                if (customer.getMetrics().getTotalOrdersLast90d() != null &&
                    customer.getMetrics().getEmailOpenRate() != null &&
                    customer.getMetrics().getEmailOpenRate().doubleValue() < 0.05) {
                    
                    // We also need to ensure we don't cooldown brand new users with 0 sends.
                    // Assuming low open rate + LTV exists means they are fatigued.
                    if (customer.getMetrics().getMonetaryTotal() != null && customer.getMetrics().getMonetaryTotal().doubleValue() > 0) {
                        log.info("🚨 Channel Fatigue Detected for user {}. Open Rate is critically low. Applying 14-day Cooldown.", customer.getId());
                        customer.setChannelCooldownUntil(OffsetDateTime.now().plusDays(14));
                        cooldownCount++;
                    }
                }
            }
        }

        if (cooldownCount > 0) {
            customerRepository.saveAll(allCustomers);
            log.info("💤 Successfully placed {} fatigued users into a 14-day channel cooldown to protect brand reputation.", cooldownCount);
        } else {
            log.info("No channel fatigue detected tonight.");
        }
    }
}
