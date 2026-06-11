package com.xenocrm.correction.service;

import com.xenocrm.correction.dto.CorrectionEventResponseDto;
import com.xenocrm.correction.mapper.CorrectionEventMapper;
import com.xenocrm.correction.repository.CorrectionEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CorrectionRetrievalService {

    private final CorrectionEventRepository correctionEventRepository;
    private final CorrectionEventMapper correctionEventMapper;

    public Page<CorrectionEventResponseDto> getCorrections(Pageable pageable) {
        return correctionEventRepository.findAll(pageable)
                .map(correctionEventMapper::toResponseDto);
    }
}
