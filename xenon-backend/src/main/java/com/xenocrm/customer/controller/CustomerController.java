package com.xenocrm.customer.controller;

import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.customer.dto.Customer360ResponseDto;
import com.xenocrm.customer.dto.CustomerCreateRequestDto;
import com.xenocrm.customer.dto.CustomerResponseDto;
import com.xenocrm.customer.dto.CustomerUpdateRequestDto;
import com.xenocrm.customer.service.CustomerIngestionService;
import com.xenocrm.customer.service.CustomerMetricsComputationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * CustomerController — Exposes customer ingestion and 360 view endpoints.
 * Layer: Controller
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Customer management endpoints")
public class CustomerController {

    private final CustomerIngestionService customerIngestionService;
    private final CustomerMetricsComputationService customerMetricsComputationService;

    @PostMapping
    @Operation(summary = "Create a new customer")
    public ResponseEntity<ResponseWrapper<CustomerResponseDto>> createCustomer(@Valid @RequestBody CustomerCreateRequestDto request) {
        CustomerResponseDto responseDto = customerIngestionService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(responseDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing customer")
    public ResponseEntity<ResponseWrapper<CustomerResponseDto>> updateCustomer(
            @PathVariable UUID id,
            @Valid @RequestBody CustomerUpdateRequestDto request) {
        CustomerResponseDto responseDto = customerIngestionService.updateCustomer(id, request);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get basic customer details")
    public ResponseEntity<ResponseWrapper<CustomerResponseDto>> getCustomer(@PathVariable UUID id) {
        CustomerResponseDto responseDto = customerIngestionService.getCustomerById(id);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto));
    }

    @GetMapping("/{id}/360")
    @Operation(summary = "Get customer 360 view including metrics")
    public ResponseEntity<ResponseWrapper<Customer360ResponseDto>> getCustomer360(@PathVariable UUID id) {
        Customer360ResponseDto responseDto = customerMetricsComputationService.getCustomer360(id);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto));
    }
}
