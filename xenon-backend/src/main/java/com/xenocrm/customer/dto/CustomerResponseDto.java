package com.xenocrm.customer.dto;

import com.xenocrm.customer.enums.CustomerGender;
import com.xenocrm.customer.enums.PreferredChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * CustomerResponseDto — Standard DTO representing a customer.
 * Layer: DTO
 * Purpose: Exposes customer entity data without exposing the entity itself.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto {
    private UUID id;
    private String externalId;
    private String email;
    private String phone;
    private String whatsappNumber;
    private String name;
    private CustomerGender gender;
    private LocalDate dateOfBirth;
    private String city;
    private String state;
    private String country;
    private String[] tags;
    private Map<String, Object> customAttributes;
    private PreferredChannel preferredChannel;
    private String[] optOutChannels;
    private boolean isGloballyOptedOut;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
