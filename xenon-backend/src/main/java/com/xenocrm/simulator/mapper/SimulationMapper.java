package com.xenocrm.simulator.mapper;

import com.xenocrm.simulator.dto.SimulationRunResultDto;
import com.xenocrm.simulator.entity.SimulationRunEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SimulationMapper {

    @Mapping(source = "campaign.id", target = "campaignId")
    @Mapping(source = "winningVariant.id", target = "winningVariantId")
    SimulationRunResultDto toResultDto(SimulationRunEntity entity);
}
