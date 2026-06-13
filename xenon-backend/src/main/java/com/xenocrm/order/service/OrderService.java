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
import com.xenocrm.communication.entity.CommunicationEntity;
import com.xenocrm.communication.repository.CommunicationRepository;
import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.communication.enums.CommunicationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;
    private final CommunicationRepository communicationRepository;
    private final CampaignRepository campaignRepository;

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
        
        // --- ORDER ATTRIBUTION LOGIC ---
        try {
            // Find the most recent communication for this customer within the last 72 hours that was clicked/opened/delivered
            Page<CommunicationEntity> recentComms = communicationRepository.findAllByCustomerId(
                customer.getId(), 
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "sentAt"))
            );
            
            if (!recentComms.isEmpty()) {
                CommunicationEntity comm = recentComms.getContent().get(0);
                OffsetDateTime attributionWindowStart = OffsetDateTime.now().minusHours(comm.getAttributionWindowHours());
                
                if (comm.getSentAt() != null && comm.getSentAt().isAfter(attributionWindowStart)) {
                    log.info("Attributing order {} to communication {}", savedOrder.getId(), comm.getId());
                    
                    // Mark Communication as CONVERTED (ORDER_ATTRIBUTED concept)
                    comm.setStatus(CommunicationStatus.CONVERTED);
                    comm.setConvertedAt(OffsetDateTime.now());
                    comm.setAttributedOrder(savedOrder);
                    communicationRepository.save(comm);
                    
                    // Update Campaign Metrics
                    CampaignEntity campaign = comm.getCampaign();
                    if (campaign != null) {
                        campaign.setTotalConverted(campaign.getTotalConverted() + 1);
                        java.math.BigDecimal currentRevenue = campaign.getRevenueAttributed() != null ? campaign.getRevenueAttributed() : java.math.BigDecimal.ZERO;
                        campaign.setRevenueAttributed(currentRevenue.add(savedOrder.getTotalAmount()));
                        campaignRepository.save(campaign);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to attribute order {}: {}", savedOrder.getId(), e.getMessage());
        }
        
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
