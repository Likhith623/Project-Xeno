package com.xenocrm.customer.repository;

import com.xenocrm.customer.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * CustomerRepository -- JPA repository for CustomerEntity.
 * Layer: Repository
 */
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {
    /** Finds a customer by their unique email address. */
    Optional<CustomerEntity> findByEmail(String email);
    /** Finds a customer by their phone number. */
    Optional<CustomerEntity> findByPhone(String phone);
    /** Finds a customer by the brand external ID. */
    Optional<CustomerEntity> findByExternalId(String externalId);
    /** Returns all non-opted-out customers, paginated. */
    Page<CustomerEntity> findAllByIsGloballyOptedOutFalse(Pageable pageable);
    /** Finds customers whose tags array contains the given tag value. */
    @Query(value = "SELECT * FROM customers WHERE :tag = ANY(tags)", nativeQuery = true)
    List<CustomerEntity> findAllByTag(@Param("tag") String tag);
}
