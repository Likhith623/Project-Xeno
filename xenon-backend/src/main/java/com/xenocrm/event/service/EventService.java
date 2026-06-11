package com.xenocrm.event.service;

import com.xenocrm.customer.entity.CustomerEntity;
import com.xenocrm.customer.repository.CustomerRepository;
import com.xenocrm.event.dto.EventCreateRequestDto;
import com.xenocrm.event.dto.EventResponseDto;
import com.xenocrm.event.entity.EventEntity;
import com.xenocrm.event.mapper.EventMapper;
import com.xenocrm.event.repository.EventRepository;
import com.xenocrm.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * EventService — Handles ingestion and retrieval of events.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final CustomerRepository customerRepository;
    private final EventMapper eventMapper;

    @Transactional
    public EventResponseDto createEvent(EventCreateRequestDto request) {
        log.debug("Ingesting new event of type: {}", request.getEventType());

        EventEntity event = eventMapper.toEntity(request);

        if (request.getCustomerId() != null) {
            CustomerEntity customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", request.getCustomerId()));
            event.setCustomer(customer);
        }

        EventEntity savedEvent = eventRepository.save(event);
        return eventMapper.toResponseDto(savedEvent);
    }

    @Transactional(readOnly = true)
    public List<EventResponseDto> getEventsByCustomerId(UUID customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer", "id", customerId);
        }

        return eventRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(eventMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
