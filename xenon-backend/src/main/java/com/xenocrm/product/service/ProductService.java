package com.xenocrm.product.service;

import com.xenocrm.constants.XenoCrmConstants;
import com.xenocrm.exception.ResourceNotFoundException;
import com.xenocrm.exception.ValidationException;
import com.xenocrm.product.dto.ProductCreateRequestDto;
import com.xenocrm.product.dto.ProductResponseDto;
import com.xenocrm.product.entity.ProductCategoryEntity;
import com.xenocrm.product.entity.ProductEntity;
import com.xenocrm.product.mapper.ProductMapper;
import com.xenocrm.product.repository.ProductCategoryRepository;
import com.xenocrm.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * ProductService — Handles product creation and retrieval.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponseDto createProduct(ProductCreateRequestDto request) {
        log.debug("Creating new product: {}", request.getName());

        if (request.getExternalId() != null && productRepository.findByExternalId(request.getExternalId()).isPresent()) {
            throw new ValidationException("Product with externalId " + request.getExternalId() + " already exists");
        }
        if (request.getSku() != null && productRepository.findBySku(request.getSku()).isPresent()) {
            throw new ValidationException("Product with sku " + request.getSku() + " already exists");
        }

        ProductEntity product = productMapper.toEntity(request);
        
        if (request.getCurrency() == null) {
            product.setCurrency(XenoCrmConstants.DEFAULT_CURRENCY);
        }
        
        if (request.getIsActive() != null) {
            product.setActive(request.getIsActive());
        } else {
            product.setActive(true);
        }

        if (request.getCategoryId() != null) {
            ProductCategoryEntity category = productCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProductCategory", "id", request.getCategoryId()));
            product.setCategory(category);
        }

        if (product.getTags() == null) product.setTags(new String[0]);

        ProductEntity savedProduct = productRepository.save(product);
        return productMapper.toResponseDto(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(UUID id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return productMapper.toResponseDto(product);
    }
}
