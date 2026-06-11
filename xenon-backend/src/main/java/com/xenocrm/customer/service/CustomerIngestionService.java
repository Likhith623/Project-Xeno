package com.xenocrm.customer.service;

import com.xenocrm.customer.dto.CustomerCreateRequestDto;
import com.xenocrm.customer.dto.CustomerResponseDto;
import com.xenocrm.customer.dto.CustomerUpdateRequestDto;
import com.xenocrm.customer.entity.CustomerEntity;
import com.xenocrm.customer.mapper.CustomerMapper;
import com.xenocrm.customer.repository.CustomerRepository;
import com.xenocrm.exception.ResourceNotFoundException;
import com.xenocrm.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * CustomerIngestionService — Handles creation and updates of customers.
 * Layer: Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerIngestionService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    public CustomerResponseDto createCustomer(CustomerCreateRequestDto request) {
        log.debug("Creating new customer with email: {}", request.getEmail());

        if (request.getEmail() != null && customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ValidationException("Customer with email " + request.getEmail() + " already exists");
        }
        if (request.getExternalId() != null && customerRepository.findByExternalId(request.getExternalId()).isPresent()) {
            throw new ValidationException("Customer with externalId " + request.getExternalId() + " already exists");
        }
        if (request.getPhone() != null && customerRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new ValidationException("Customer with phone " + request.getPhone() + " already exists");
        }

        CustomerEntity customer = customerMapper.toEntity(request);
        
        // Ensure arrays are not null
        if (customer.getTags() == null) customer.setTags(new String[0]);
        if (customer.getOptOutChannels() == null) customer.setOptOutChannels(new String[0]);
        
        CustomerEntity savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponseDto(savedCustomer);
    }

    @Transactional
    public CustomerResponseDto updateCustomer(UUID id, CustomerUpdateRequestDto request) {
        log.debug("Updating customer with id: {}", id);

        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        customerMapper.updateEntityFromDto(request, customer);
        
        CustomerEntity updatedCustomer = customerRepository.save(customer);
        return customerMapper.toResponseDto(updatedCustomer);
    }

    @Transactional(readOnly = true)
    public CustomerResponseDto getCustomerById(UUID id) {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        return customerMapper.toResponseDto(customer);
    }

    @Transactional
    public void deleteCustomer(UUID id) {
        log.debug("Deleting customer with id: {}", id);
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer", "id", id);
        }
        customerRepository.deleteById(id);
    }
}
