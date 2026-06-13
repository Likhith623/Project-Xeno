package com.xenocrm.campaign.service;

import com.xenocrm.customer.entity.CustomerEntity;
import org.springframework.stereotype.Service;

@Service
public class DynamicDiscountService {

    /**
     * Recommends a discount tier based on LTV/RFM and Churn Risk.
     * VIPs get 5%, Churn risks get 25%, standard gets 10%.
     * 
     * @param customer the customer entity
     * @return discount string (e.g. "5%", "10%", "25%")
     */
    public String calculateDiscountTier(CustomerEntity customer) {
        if (customer.getMetrics() == null) {
            return "10%"; // default
        }

        double churnRisk = customer.getMetrics().getChurnProbability() != null ? customer.getMetrics().getChurnProbability().doubleValue() : 0.0;
        double rfmScore = customer.getMetrics().getRfmScore() != null ? customer.getMetrics().getRfmScore().doubleValue() : 50.0;

        if (churnRisk > 0.7) {
            return "25%"; // Retention priority
        } else if (rfmScore > 80.0) {
            return "5%";  // VIP, high loyalty, low discount protects margin
        } else {
            return "10%"; // Standard
        }
    }
}
