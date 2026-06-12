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

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.xenocrm.common.PaginationMetadata;

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
    private final com.xenocrm.order.service.OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new customer")
    public ResponseEntity<ResponseWrapper<CustomerResponseDto>> createCustomer(@Valid @RequestBody CustomerCreateRequestDto request) {
        CustomerResponseDto responseDto = customerIngestionService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(responseDto));
    }

    @PostMapping("/bulk")
    @Operation(summary = "Create multiple customers in bulk")
    public ResponseEntity<ResponseWrapper<List<CustomerResponseDto>>> createCustomersBulk(@Valid @RequestBody List<CustomerCreateRequestDto> requests) {
        List<CustomerResponseDto> responseDtos = customerIngestionService.bulkCreateCustomers(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(responseDtos));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing customer")
    public ResponseEntity<ResponseWrapper<CustomerResponseDto>> updateCustomer(
            @PathVariable UUID id,
            @Valid @RequestBody CustomerUpdateRequestDto request) {
        CustomerResponseDto responseDto = customerIngestionService.updateCustomer(id, request);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto));
    }

    @GetMapping
    @Operation(summary = "Get all customers with pagination")
    public ResponseEntity<ResponseWrapper<List<CustomerResponseDto>>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerResponseDto> pagedResult = customerIngestionService.getAllCustomers(pageable);
        return ResponseEntity.ok(ResponseWrapper.success(
                pagedResult.getContent(),
                PaginationMetadata.from(pagedResult)
        ));
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

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a customer")
    public ResponseEntity<ResponseWrapper<Void>> deleteCustomer(@PathVariable UUID id) {
        customerIngestionService.deleteCustomer(id);
        return ResponseEntity.ok(ResponseWrapper.success(null));
    }

    @GetMapping("/by-email")
    public ResponseEntity<ResponseWrapper<CustomerResponseDto>> getCustomerByEmail(@RequestParam String email) {
        CustomerResponseDto responseDto = customerIngestionService.getCustomerByEmail(email);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto));
    }

    @GetMapping("/by-tag")
    @Operation(summary = "Get customers by tag")
    public ResponseEntity<ResponseWrapper<List<CustomerResponseDto>>> getCustomersByTag(
            @RequestParam String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerResponseDto> pagedResult = customerIngestionService.getCustomersByTag(tag, pageable);
        return ResponseEntity.ok(ResponseWrapper.success(
                pagedResult.getContent(),
                PaginationMetadata.from(pagedResult)
        ));
    }

    @GetMapping("/{id}/orders")
    @Operation(summary = "Get customer orders")
    public ResponseEntity<ResponseWrapper<List<com.xenocrm.order.dto.OrderResponseDto>>> getCustomerOrders(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<com.xenocrm.order.dto.OrderResponseDto> pagedResult = orderService.getOrdersByCustomerId(id, pageable);
        return ResponseEntity.ok(ResponseWrapper.success(
                pagedResult.getContent(),
                PaginationMetadata.from(pagedResult)
        ));
    }
}
