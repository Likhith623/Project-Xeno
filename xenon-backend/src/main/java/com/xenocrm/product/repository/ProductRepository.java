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
    Optional<ProductEntity> findByExternalId(String externalId);
    Optional<ProductEntity> findBySku(String sku);
}
