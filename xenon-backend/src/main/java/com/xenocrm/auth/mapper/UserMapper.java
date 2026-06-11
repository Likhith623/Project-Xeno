package com.xenocrm.auth.mapper;

import com.xenocrm.auth.dto.UserCreateRequestDto;
import com.xenocrm.auth.dto.UserResponseDto;
import com.xenocrm.auth.entity.RoleEntity;
import com.xenocrm.auth.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * UserMapper — MapStruct mapper for User domain.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "roles", ignore = true) // Handled in service
    @Mapping(target = "passwordHash", ignore = true) // Handled in service
    UserEntity toEntity(UserCreateRequestDto dto);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    UserResponseDto toResponseDto(UserEntity entity);

    @Named("mapRoles")
    default Set<String> mapRoles(Set<RoleEntity> roles) {
        if (roles == null) return null;
        return roles.stream().map(RoleEntity::getName).collect(Collectors.toSet());
    }
}
