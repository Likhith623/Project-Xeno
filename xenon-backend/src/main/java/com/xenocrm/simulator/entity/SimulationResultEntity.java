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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id")
    private SimulationPersonaEntity persona;

    @Column(name = "simulated_sends", nullable = false)
    private int simulatedSends;

    @Column(name = "simulated_opens", nullable = false)
    private int simulatedOpens;

    @Column(name = "simulated_clicks", nullable = false)
    private int simulatedClicks;

    @Column(name = "simulated_conversions", nullable = false)
    private int simulatedConversions;

    @Column(name = "simulated_revenue", precision = 12, scale = 2)
    private BigDecimal simulatedRevenue;

    @Column(name = "open_rate", insertable = false, updatable = false)
    private BigDecimal openRate;

    @Column(name = "ctr", insertable = false, updatable = false)
    private BigDecimal ctr;
}
