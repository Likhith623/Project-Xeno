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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
        if (order.getPlacedAt() == null) {
            order.setPlacedAt(java.time.OffsetDateTime.now());
        }

        OrderEntity savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDto(savedOrder);
    }

    @Transactional
    public java.util.List<OrderResponseDto> bulkCreateOrders(java.util.List<OrderCreateRequestDto> requests) {
        log.debug("Bulk creating {} orders", requests.size());
        java.util.List<OrderResponseDto> responses = new java.util.ArrayList<>();
        for (OrderCreateRequestDto request : requests) {
            try {
                responses.add(createOrder(request));
            } catch (Exception e) {
                log.warn("Failed to create order in bulk: {}", e.getMessage());
            }
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(UUID id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        return orderMapper.toResponseDto(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getOrdersByCustomerId(UUID customerId, Pageable pageable) {
        return orderRepository.findByCustomerId(customerId, pageable).map(orderMapper::toResponseDto);
    }
}
