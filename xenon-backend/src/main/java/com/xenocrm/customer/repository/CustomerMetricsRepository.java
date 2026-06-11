package com.xenocrm.customer.repository;

import com.xenocrm.customer.entity.CustomerMetricsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * CustomerMetricsRepository — Spring Data JPA repository for customer metrics.
 * Layer: Repository
 */
@Repository
public interface CustomerMetricsRepository extends JpaRepository<CustomerMetricsEntity, UUID> {
    java.util.Optional<CustomerMetricsEntity> findByCustomerId(UUID customerId);
    java.util.List<CustomerMetricsEntity> findAllByRfmScoreGreaterThanEqual(java.math.BigDecimal minimumRfmScore);
    java.util.List<CustomerMetricsEntity> findAllByChurnProbabilityGreaterThan(java.math.BigDecimal threshold);
}
