package com.xenocrm.segment.entity;

import com.xenocrm.customer.entity.CustomerEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Entity
@Table(name = "segment_members")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SegmentMemberEntity {

    @EmbeddedId
    private SegmentMemberId id;

    @CreatedDate
    @Column(name = "added_at", nullable = false, updatable = false)
    private OffsetDateTime addedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("segmentId")
    @JoinColumn(name = "segment_id")
    private AudienceSegmentEntity segment;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("customerId")
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;
}
