package com.xenocrm.customer.service;

import com.xenocrm.customer.dto.Customer360ResponseDto;
import com.xenocrm.customer.dto.CustomerCreateRequestDto;
import com.xenocrm.customer.dto.CustomerResponseDto;
import com.xenocrm.customer.dto.CustomerUpdateRequestDto;
import com.xenocrm.customer.entity.CustomerEntity;
import com.xenocrm.customer.entity.CustomerMetricsEntity;
import com.xenocrm.customer.mapper.CustomerMapper;
import com.xenocrm.customer.repository.CustomerMetricsRepository;
import com.xenocrm.customer.repository.CustomerRepository;
import com.xenocrm.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMetricsRepository customerMetricsRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    public CustomerResponseDto createCustomer(CustomerCreateRequestDto request) {
        CustomerEntity customer = customerMapper.toEntity(request);
        
        if (customer.getOptOutChannels() == null) {
            customer.setOptOutChannels(new String[0]);
        }

        CustomerEntity savedCustomer = customerRepository.save(customer);

        CustomerMetricsEntity metrics = new CustomerMetricsEntity();
        metrics.setCustomer(savedCustomer);
        metrics.setCustomerId(savedCustomer.getId());
        metrics.setLastComputedAt(OffsetDateTime.now());
        customerMetricsRepository.save(metrics);

        return customerMapper.toResponseDto(savedCustomer);
    }

    @Transactional(readOnly = true)
    public Customer360ResponseDto getCustomer360(UUID id) {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        return customerMapper.to360ResponseDto(customer, customer.getMetrics());
    }
}
