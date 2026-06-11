package com.xenocrm.simulator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * SimulationPersonaEntity — JPA entity mapping to the `simulation_personas` table.
 * Layer: Domain Entity
 */
@Entity
@Table(name = "simulation_personas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulationPersonaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "archetype", nullable = false)
    private String archetype; // 'loyalist', 'bargain_hunter', etc.

    @Column(name = "base_open_rate", nullable = false, precision = 4, scale = 3)
    private BigDecimal baseOpenRate; // e.g. 0.350

    @Column(name = "base_ctr", nullable = false, precision = 4, scale = 3)
    private BigDecimal baseCtr;

    @Column(name = "base_conversion_rate", nullable = false, precision = 4, scale = 3)
    private BigDecimal baseConversionRate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "channel_multipliers", columnDefinition = "jsonb")
    private Map<String, Object> channelMultipliers; // {"email": 1.2, "whatsapp": 0.8}

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "copy_multipliers", columnDefinition = "jsonb")
    private Map<String, Object> copyMultipliers; // {"urgency": 1.5, "discount": 1.2}
}
