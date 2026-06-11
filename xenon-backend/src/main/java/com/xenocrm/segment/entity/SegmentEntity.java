package com.xenocrm.segment.entity;

import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * SegmentEntity — JPA entity mapping to the `segments` table.
 */
@Entity
@Table(name = "segments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SegmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "query_dsl", nullable = false, columnDefinition = "text")
    private String queryDsl; // Brand-friendly JSON DSL representing the segment logic

    @Column(name = "compiled_sql", nullable = false, columnDefinition = "text")
    private String compiledSql; // The actual raw SQL WHERE clause executed against DB

    @Type(StringArrayType.class)
    @Column(name = "tags", columnDefinition = "text[]")
    private String[] tags;

    @Column(name = "is_dynamic", nullable = false)
    private boolean isDynamic; // DEFAULT true

    @Column(name = "last_evaluated_count")
    private Integer lastEvaluatedCount;

    @Column(name = "last_evaluated_at")
    private OffsetDateTime lastEvaluatedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
