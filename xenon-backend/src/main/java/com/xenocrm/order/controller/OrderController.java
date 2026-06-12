package com.xenocrm.order.controller;

import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.order.dto.OrderCreateRequestDto;
import com.xenocrm.order.dto.OrderResponseDto;
import com.xenocrm.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.xenocrm.common.PaginationMetadata;

/**
 * OrderController — Exposes order ingestion and retrieval endpoints.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order management endpoints")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<ResponseWrapper<OrderResponseDto>> createOrder(@Valid @RequestBody OrderCreateRequestDto request) {
        OrderResponseDto responseDto = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(responseDto));
    }

    @PostMapping("/bulk")
    @Operation(summary = "Create multiple orders in bulk")
    public ResponseEntity<ResponseWrapper<List<OrderResponseDto>>> createOrdersBulk(@Valid @RequestBody List<OrderCreateRequestDto> requests) {
        List<OrderResponseDto> responseDtos = orderService.bulkCreateOrders(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(responseDtos));
    }

    @GetMapping
    @Operation(summary = "Get all orders with pagination")
    public ResponseEntity<ResponseWrapper<List<OrderResponseDto>>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponseDto> pagedResult = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(ResponseWrapper.success(
                pagedResult.getContent(),
                PaginationMetadata.from(pagedResult)
        ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order details")
    public ResponseEntity<ResponseWrapper<OrderResponseDto>> getOrder(@PathVariable UUID id) {
        OrderResponseDto responseDto = orderService.getOrderById(id);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto));
    }
}
