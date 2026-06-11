package com.xenocrm.segment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SegmentMemberId implements Serializable {

    @Column(name = "segment_id")
    private UUID segmentId;

    @Column(name = "customer_id")
    private UUID customerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SegmentMemberId that = (SegmentMemberId) o;
        return Objects.equals(segmentId, that.segmentId) &&
               Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(segmentId, customerId);
    }
}
