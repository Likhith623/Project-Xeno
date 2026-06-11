package com.xenocrm.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ResponseWrapper — Standardized API response format.
 * Layer: Common
 * Purpose: Ensures all API responses have a consistent shape.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper<T> {
    
    private boolean success;
    private T data;
    private String errorCode;
    private String errorMessage;
    private PaginationMetadata pagination;

    public static <T> ResponseWrapper<T> success(T data) {
        return ResponseWrapper.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> ResponseWrapper<T> success(T data, PaginationMetadata pagination) {
        return ResponseWrapper.<T>builder()
                .success(true)
                .data(data)
                .pagination(pagination)
                .build();
    }

    public static <T> ResponseWrapper<T> error(String errorCode, String errorMessage) {
        return ResponseWrapper.<T>builder()
                .success(false)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }
}
