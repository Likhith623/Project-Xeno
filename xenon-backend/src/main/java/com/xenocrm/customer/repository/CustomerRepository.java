package com.xenocrm.customer.repository;

import com.xenocrm.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * CustomerRepository — Spring Data JPA repository for customers.
 * Layer: Repository
 */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {
    Optional<CustomerEntity> findByEmail(String email);
    Optional<CustomerEntity> findByPhone(String phone);
    Optional<CustomerEntity> findByExternalId(String externalId);
    org.springframework.data.domain.Page<CustomerEntity> findAllByIsGloballyOptedOutFalse(org.springframework.data.domain.Pageable pageable);

    /** Finds customers whose tags array contains the given tag value. */
    @org.springframework.data.jpa.repository.Query(value="SELECT * FROM customers WHERE :tag = ANY(tags)", nativeQuery=true)
    java.util.List<CustomerEntity> findAllByTag(@org.springframework.data.repository.query.Param("tag") String tag);
}
