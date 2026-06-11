package com.xenocrm.memory.mapper;

import com.xenocrm.memory.dto.OrgMemoryEntryDto;
import com.xenocrm.memory.entity.OrgMemoryEntryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrgMemoryMapper {
    OrgMemoryEntryDto toDto(OrgMemoryEntryEntity entity);
}
