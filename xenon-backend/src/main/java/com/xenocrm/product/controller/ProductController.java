package com.xenocrm.product.controller;

import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.product.dto.ProductCreateRequestDto;
import com.xenocrm.product.dto.ProductResponseDto;
import com.xenocrm.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * ProductController — Exposes product ingestion and retrieval endpoints.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product catalog management endpoints")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<ResponseWrapper<ProductResponseDto>> createProduct(@Valid @RequestBody ProductCreateRequestDto request) {
        ProductResponseDto responseDto = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(responseDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product details")
    public ResponseEntity<ResponseWrapper<ProductResponseDto>> getProduct(@PathVariable UUID id) {
        ProductResponseDto responseDto = productService.getProductById(id);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto));
    }
}
