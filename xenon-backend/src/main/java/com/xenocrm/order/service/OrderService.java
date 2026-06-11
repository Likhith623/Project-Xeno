package com.xenocrm.order.service;

import com.xenocrm.customer.entity.CustomerEntity;
import com.xenocrm.customer.repository.CustomerRepository;
import com.xenocrm.exception.ResourceNotFoundException;
import com.xenocrm.order.dto.OrderCreateRequestDto;
import com.xenocrm.order.dto.OrderResponseDto;
import com.xenocrm.order.entity.OrderEntity;
import com.xenocrm.order.enums.OrderStatus;
import com.xenocrm.order.mapper.OrderMapper;
import com.xenocrm.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponseDto createOrder(OrderCreateRequestDto request) {
        CustomerEntity customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", request.getCustomerId()));

        OrderEntity order = orderMapper.toEntity(request);
        order.setCustomer(customer);
        
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.PENDING);
        }

        OrderEntity savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDto(savedOrder);
    }
}
