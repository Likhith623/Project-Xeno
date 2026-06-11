package com.xenocrm.simulator.entity;

import com.xenocrm.variant.entity.MessageVariantEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * SimulationResultEntity — JPA entity mapping to the `simulation_results` table.
 * Layer: Domain Entity
 */
@Entity
@Table(name = "simulation_results")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulationResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "run_id")
    private SimulationRunEntity run;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id")
    private MessageVariantEntity variant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "persona_id")
    private SimulationPersonaEntity persona;

    @Column(name = "simulated_impressions", nullable = false)
    private Integer simulatedImpressions;

    @Column(name = "simulated_opens", nullable = false)
    private Integer simulatedOpens;

    @Column(name = "simulated_clicks", nullable = false)
    private Integer simulatedClicks;

    @Column(name = "simulated_conversions", nullable = false)
    private Integer simulatedConversions;

    @Column(name = "expected_revenue", precision = 12, scale = 2)
    private BigDecimal expectedRevenue;
}
