package com.xenocrm.customer.dto;

import com.xenocrm.customer.enums.CustomerGender;
import com.xenocrm.customer.enums.PreferredChannel;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class Customer360ResponseDto {
    private UUID id;
    private String phone;
    private String email;
    private String name;
    private CustomerGender gender;
    private LocalDate dateOfBirth;
    private String city;
    private String country;
    private String[] optOutChannels;
    private Map<String, Object> customAttributes;
    private PreferredChannel preferredChannel;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    
    private Integer totalOrders;
    private BigDecimal totalSpent;
    private OffsetDateTime lastOrderDate;
    private BigDecimal aov;
    private BigDecimal purchaseFrequency;
    private BigDecimal churnProbability;
    private BigDecimal predictedLtv;
}
