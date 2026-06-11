package com.xenocrm.product.repository;

import com.xenocrm.product.entity.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * ProductCategoryRepository — Spring Data JPA repository.
 */
@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, UUID> {
    Optional<ProductCategoryEntity> findBySlug(String slug);
}
