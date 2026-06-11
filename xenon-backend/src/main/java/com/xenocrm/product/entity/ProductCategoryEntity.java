package com.xenocrm.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

/**
 * ProductCategoryEntity -- JPA entity mapping to the `product_categories` table.
 * Layer: Domain Entity
 * Purpose: Taxonomy tree for product classification (e.g. electronics > phones > flagship).
 */
@Entity
@Table(name = "product_categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;                        // NOT NULL -- category display name

    @Column(name = "slug", unique = true, nullable = false)
    private String slug;                        // UNIQUE NOT NULL -- URL-safe identifier

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ProductCategoryEntity parentCategory; // nullable -- parent in the category tree

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
    private List<ProductCategoryEntity> childCategories; // sub-categories
}
