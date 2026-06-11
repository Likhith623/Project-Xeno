package com.xenocrm.customer.dto;

import com.xenocrm.customer.enums.CustomerGender;
import com.xenocrm.customer.enums.PreferredChannel;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

/**
 * CustomerUpdateRequestDto — DTO for updating an existing customer.
 * Layer: DTO
 * Purpose: Carries incoming data for customer updates.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateRequestDto {

    private String externalId;

    @Email
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
    private Boolean isGloballyOptedOut;
}
