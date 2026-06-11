package com.xenocrm.customer.dto;

import com.xenocrm.customer.enums.CustomerGender;
import com.xenocrm.customer.enums.PreferredChannel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.Map;

/** CustomerCreateRequestDto -- DTO for ingesting a new customer. Layer: DTO */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CustomerCreateRequestDto {
    private String externalId;
    @Email private String email;
    private String phone;
    private String whatsappNumber;
    @NotBlank private String name;
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
}
