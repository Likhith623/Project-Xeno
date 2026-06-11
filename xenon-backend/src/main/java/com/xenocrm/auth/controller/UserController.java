package com.xenocrm.auth.controller;

import com.xenocrm.auth.dto.UserCreateRequestDto;
import com.xenocrm.auth.dto.UserResponseDto;
import com.xenocrm.auth.service.UserService;
import com.xenocrm.common.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * UserController — Exposes user management endpoints.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User management endpoints")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<ResponseWrapper<UserResponseDto>> createUser(@Valid @RequestBody UserCreateRequestDto request) {
        UserResponseDto responseDto = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(responseDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user details by ID")
    public ResponseEntity<ResponseWrapper<UserResponseDto>> getUser(@PathVariable UUID id) {
        UserResponseDto responseDto = userService.getUserById(id);
        return ResponseEntity.ok(ResponseWrapper.success(responseDto));
    }
}
