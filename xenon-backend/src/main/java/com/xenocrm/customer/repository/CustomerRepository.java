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
    Optional<CustomerEntity> findByExternalId(String externalId);
    Optional<CustomerEntity> findByPhone(String phone);
}
