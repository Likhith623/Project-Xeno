package com.xenocrm.customer.dto;

import com.xenocrm.customer.enums.CustomerGender;
import com.xenocrm.customer.enums.PreferredChannel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;
import java.util.Map;

@Data
public class CustomerCreateRequestDto {
    @NotBlank
    private String name;
    private String phone;
    @Email
    private String email;
    private CustomerGender gender;
    private LocalDate dateOfBirth;
    private String city;
    private String country;
    private String[] optOutChannels;
    private Map<String, Object> customAttributes;
    private PreferredChannel preferredChannel;
}
