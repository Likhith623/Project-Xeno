package com.xenocrm.order.service;

import com.xenocrm.constants.XenoCrmConstants;
import com.xenocrm.customer.entity.CustomerEntity;
import com.xenocrm.customer.repository.CustomerRepository;
import com.xenocrm.exception.ResourceNotFoundException;
import com.xenocrm.exception.ValidationException;
import com.xenocrm.order.dto.OrderCreateRequestDto;
import com.xenocrm.order.dto.OrderItemRequestDto;
import com.xenocrm.order.dto.OrderResponseDto;
import com.xenocrm.order.entity.OrderEntity;
import com.xenocrm.order.entity.OrderItemEntity;
import com.xenocrm.order.enums.OrderStatus;
import com.xenocrm.order.mapper.OrderMapper;
import com.xenocrm.order.repository.OrderRepository;
import com.xenocrm.product.entity.ProductEntity;
import com.xenocrm.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * OrderService — Handles order creation and retrieval.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponseDto createOrder(OrderCreateRequestDto request) {
        log.debug("Creating new order with externalId: {}", request.getExternalId());

        if (orderRepository.findByExternalId(request.getExternalId()).isPresent()) {
            throw new ValidationException("Order with externalId " + request.getExternalId() + " already exists");
        }

        CustomerEntity customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", request.getCustomerId()));

        OrderEntity order = orderMapper.toEntity(request);
        order.setCustomer(customer);

        if (order.getCurrency() == null) {
            order.setCurrency(XenoCrmConstants.DEFAULT_CURRENCY);
        }
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.COMPLETED);
        }
        if (order.getOrderDate() == null) {
            order.setOrderDate(OffsetDateTime.now());
        }

        for (OrderItemRequestDto itemRequest : request.getItems()) {
            ProductEntity product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", itemRequest.getProductId()));

            OrderItemEntity item = orderMapper.toItemEntity(itemRequest);
            item.setProduct(product);
            
            if (item.getQuantity() == null) {
                item.setQuantity(1);
            }
            
            order.addItem(item);
        }

        OrderEntity savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(UUID id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        return orderMapper.toResponseDto(order);
    }
}
