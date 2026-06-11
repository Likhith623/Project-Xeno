package com.xenocrm.ml.controller;

import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.ml.dto.ModelTrainingLogDto;
import com.xenocrm.ml.service.ModelTrainingLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * ModelTrainingLogController — Exposes ML training log endpoints.
 */
@RestController
@RequestMapping("/api/v1/ml/logs")
@RequiredArgsConstructor
@Tag(name = "ML", description = "Machine Learning tracking endpoints")
public class ModelTrainingLogController {

    private final ModelTrainingLogService modelTrainingLogService;

    @PostMapping
    @Operation(summary = "Start or log a new model training")
    public ResponseEntity<ResponseWrapper<ModelTrainingLogDto>> logTraining(@Valid @RequestBody ModelTrainingLogDto request) {
        ModelTrainingLogDto responseDto = modelTrainingLogService.logTraining(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(responseDto));
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Mark a model training as complete with metrics")
    public ResponseEntity<ResponseWrapper<ModelTrainingLogDto>> completeTraining(
            @PathVariable UUID id,
            @RequestParam String status,
            @RequestBody(required = false) Map<String, Object> metrics) {
        ModelTrainingLogDto responseDto = modelTrainingLogService.completeTraining(id, status, metrics);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto));
    }

    @GetMapping("/model/{modelName}")
    @Operation(summary = "Get logs for a specific model")
    public ResponseEntity<ResponseWrapper<List<ModelTrainingLogDto>>> getLogsByModelName(@PathVariable String modelName) {
        List<ModelTrainingLogDto> responseDtos = modelTrainingLogService.getLogsByModelName(modelName);
        return ResponseEntity.ok(ResponseWrapper.success(responseDtos));
    }
}
