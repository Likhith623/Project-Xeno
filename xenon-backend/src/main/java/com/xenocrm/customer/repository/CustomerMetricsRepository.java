package com.xenocrm.customer.repository;

import com.xenocrm.customer.entity.CustomerMetricsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * CustomerMetricsRepository -- JPA repository for CustomerMetricsEntity.
 * Layer: Repository
 */
public interface CustomerMetricsRepository extends JpaRepository<CustomerMetricsEntity, UUID> {
    /** Finds computed metrics for a specific customer. */
    Optional<CustomerMetricsEntity> findByCustomerId(UUID customerId);
    /** Finds all customers with RFM score at or above the given minimum. */
    List<CustomerMetricsEntity> findAllByRfmScoreGreaterThanEqual(BigDecimal minimumRfmScore);
    /** Finds all customers with churn probability above the given threshold. */
    List<CustomerMetricsEntity> findAllByChurnProbabilityGreaterThan(BigDecimal threshold);
    /** Finds the absolute top 100 VIP customers by RFM Score. */
    List<CustomerMetricsEntity> findTop100ByOrderByRfmScoreDesc();
    /** Finds slipping Whales for VIP Concierge Escalation. */
    List<CustomerMetricsEntity> findByMonetaryTotalGreaterThanAndRecencyDaysGreaterThan(BigDecimal minMonetary, Integer minRecency);
    
    /** 
     * Phase 8: Micro-Churn Velocity Finder (The Whisperer)
     * Finds users whose recency is 5 to 10 days slower than their personal average velocity.
     */
    @Query(value = "SELECT * FROM customer_metrics WHERE avg_days_between_orders > 0 AND recency_days >= (avg_days_between_orders + 5) AND recency_days <= (avg_days_between_orders + 10)", nativeQuery = true)
    List<CustomerMetricsEntity> findMicroChurnSlippingUsers();
}
