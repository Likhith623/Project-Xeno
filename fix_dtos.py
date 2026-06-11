import os

def write_file(path, content):
    with open(path, "w") as f:
        f.write(content.strip() + "\n")

# Customer
write_file(r"c:\Users\Sarishma\Project-Xeno\xenon-backend\src\main\java\com\xenocrm\customer\mapper\CustomerMapper.java", """
package com.xenocrm.customer.mapper;

import com.xenocrm.customer.dto.Customer360ResponseDto;
import com.xenocrm.customer.dto.CustomerCreateRequestDto;
import com.xenocrm.customer.dto.CustomerResponseDto;
import com.xenocrm.customer.dto.CustomerUpdateRequestDto;
import com.xenocrm.customer.entity.CustomerEntity;
import com.xenocrm.customer.entity.CustomerMetricsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
    CustomerEntity toEntity(CustomerCreateRequestDto dto);
    void updateEntityFromDto(CustomerUpdateRequestDto dto, @MappingTarget CustomerEntity entity);
    CustomerResponseDto toResponseDto(CustomerEntity entity);
    Customer360ResponseDto to360ResponseDto(CustomerEntity customer, CustomerMetricsEntity metrics);
}
""")

write_file(r"c:\Users\Sarishma\Project-Xeno\xenon-backend\src\main\java\com\xenocrm\customer\dto\Customer360ResponseDto.java", """
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
""")

# Order
write_file(r"c:\Users\Sarishma\Project-Xeno\xenon-backend\src\main\java\com\xenocrm\order\mapper\OrderMapper.java", """
package com.xenocrm.order.mapper;

import com.xenocrm.order.dto.OrderCreateRequestDto;
import com.xenocrm.order.dto.OrderResponseDto;
import com.xenocrm.order.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {
    @Mapping(target = "customer", ignore = true)
    OrderEntity toEntity(OrderCreateRequestDto dto);
    
    @Mapping(target = "customerId", source = "customer.id")
    OrderResponseDto toResponseDto(OrderEntity entity);
}
""")

write_file(r"c:\Users\Sarishma\Project-Xeno\xenon-backend\src\main\java\com\xenocrm\order\dto\OrderResponseDto.java", """
package com.xenocrm.order.dto;

import com.xenocrm.order.enums.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class OrderResponseDto {
    private UUID id;
    private UUID customerId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
""")

# Product
write_file(r"c:\Users\Sarishma\Project-Xeno\xenon-backend\src\main\java\com\xenocrm\product\mapper\ProductMapper.java", """
package com.xenocrm.product.mapper;

import com.xenocrm.product.dto.ProductCreateRequestDto;
import com.xenocrm.product.dto.ProductResponseDto;
import com.xenocrm.product.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    ProductEntity toEntity(ProductCreateRequestDto dto);
    
    @Mapping(target = "categoryId", source = "category.id")
    ProductResponseDto toResponseDto(ProductEntity entity);
}
""")

write_file(r"c:\Users\Sarishma\Project-Xeno\xenon-backend\src\main\java\com\xenocrm\product\dto\ProductResponseDto.java", """
package com.xenocrm.product.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class ProductResponseDto {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private UUID categoryId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
""")

print("Rewritten Mappers and DTOs successfully.")
