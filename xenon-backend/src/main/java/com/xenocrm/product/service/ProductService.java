package com.xenocrm.product.service;

import com.xenocrm.product.dto.ProductCreateRequestDto;
import com.xenocrm.product.dto.ProductResponseDto;
import com.xenocrm.product.entity.ProductCategoryEntity;
import com.xenocrm.product.entity.ProductEntity;
import com.xenocrm.product.mapper.ProductMapper;
import com.xenocrm.product.repository.ProductCategoryRepository;
import com.xenocrm.product.repository.ProductRepository;
import com.xenocrm.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponseDto createProduct(ProductCreateRequestDto request) {
        ProductCategoryEntity category = null;
        if (request.getCategoryId() != null) {
            category = productCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProductCategory", "id", request.getCategoryId()));
        }

        ProductEntity product = productMapper.toEntity(request);
        product.setCategory(category);
        
        ProductEntity savedProduct = productRepository.save(product);
        return productMapper.toResponseDto(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(UUID id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return productMapper.toResponseDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public java.util.List<com.xenocrm.product.dto.ProductCategoryResponseDto> getAllCategories() {
        return productCategoryRepository.findAll().stream().map(productMapper::toCategoryResponseDto).toList();
    }

    @Transactional
    public java.util.List<ProductResponseDto> bulkCreateProducts(java.util.List<ProductCreateRequestDto> requests) {
        log.debug("Bulk creating {} products", requests.size());
        java.util.List<ProductResponseDto> responses = new java.util.ArrayList<>();
        for (ProductCreateRequestDto request : requests) {
            try {
                responses.add(createProduct(request));
            } catch (Exception e) {
                log.warn("Failed to create product in bulk: {}", e.getMessage());
            }
        }
        return responses;
    }

    @Transactional
    public ProductResponseDto updateProduct(UUID id, java.util.Map<String, Object> updates) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        if (updates.containsKey("name")) product.setName((String) updates.get("name"));
        if (updates.containsKey("price")) product.setPrice(new java.math.BigDecimal(updates.get("price").toString()));
        if (updates.containsKey("currency")) product.setCurrency((String) updates.get("currency"));
        if (updates.containsKey("brand")) product.setBrand((String) updates.get("brand"));
        if (updates.containsKey("active")) product.setActive((Boolean) updates.get("active"));
        if (updates.containsKey("isActive")) product.setActive((Boolean) updates.get("isActive"));
        return productMapper.toResponseDto(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public java.util.List<ProductResponseDto> getProductsByCategory(UUID categoryId) {
        return productRepository.findByCategory_Id(categoryId).stream()
                .map(productMapper::toResponseDto).toList();
    }
}
