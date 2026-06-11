package com.xenocrm.customer.service;

import com.xenocrm.customer.dto.Customer360ResponseDto;
import com.xenocrm.customer.entity.CustomerEntity;
import com.xenocrm.customer.entity.CustomerMetricsEntity;
import com.xenocrm.customer.mapper.CustomerMapper;
import com.xenocrm.customer.repository.CustomerMetricsRepository;
import com.xenocrm.customer.repository.CustomerRepository;
import com.xenocrm.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * CustomerMetricsComputationService — Calculates RFM and other metrics.
 * Layer: Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerMetricsComputationService {

    private final CustomerRepository customerRepository;
    private final CustomerMetricsRepository customerMetricsRepository;
    private final CustomerMapper customerMapper;

    @Transactional(readOnly = true)
    public Customer360ResponseDto getCustomer360(UUID id) {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        
        CustomerMetricsEntity metrics = customer.getMetrics();
        // If metrics is null, we can return empty metrics or compute them sync. 
        // For now, we return null metrics fields.
        return customerMapper.to360ResponseDto(customer, metrics);
    }

    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Void> computeMetricsAsync(UUID customerId) {
        log.debug("Asynchronously computing metrics for customer: {}", customerId);
        
        return CompletableFuture.supplyAsync(() -> {
            CustomerEntity customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));

            CustomerMetricsEntity metrics = customer.getMetrics();
            if (metrics == null) {
                metrics = new CustomerMetricsEntity();
                metrics.setCustomer(customer);
                metrics.setCustomerId(customer.getId());
            }

            // In a real application, this would query the orders table.
            // For now, we'll just update the last computed timestamp.
            metrics.setLastComputedAt(OffsetDateTime.now());
            
            customerMetricsRepository.save(metrics);
            log.debug("Successfully computed metrics for customer: {}", customerId);
            return null;
        });
    }
}
