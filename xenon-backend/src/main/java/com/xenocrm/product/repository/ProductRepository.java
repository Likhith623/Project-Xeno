package com.xenocrm.product.repository;

import com.xenocrm.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * ProductRepository — Spring Data JPA repository.
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    Optional<ProductEntity> findBySku(String sku);

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT p.category FROM ProductEntity p WHERE p.category IS NOT NULL")
    java.util.List<String> findDistinctCategories();
}
