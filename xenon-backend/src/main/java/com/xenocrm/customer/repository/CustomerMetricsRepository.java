package com.xenocrm.customer.repository;

import com.xenocrm.customer.entity.CustomerMetricsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
