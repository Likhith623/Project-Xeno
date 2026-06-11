package com.xenocrm.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * UserResponseDto — Standard DTO representing a user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private UUID id;
    private String username;
    private String email;
    private boolean isActive;
    private OffsetDateTime lastLogin;
    private Set<String> roles;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
