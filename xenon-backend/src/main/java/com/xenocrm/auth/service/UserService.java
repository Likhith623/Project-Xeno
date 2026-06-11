package com.xenocrm.auth.service;

import com.xenocrm.auth.dto.UserCreateRequestDto;
import com.xenocrm.auth.dto.UserResponseDto;
import com.xenocrm.auth.entity.RoleEntity;
import com.xenocrm.auth.entity.UserEntity;
import com.xenocrm.auth.mapper.UserMapper;
import com.xenocrm.auth.repository.RoleRepository;
import com.xenocrm.auth.repository.UserRepository;
import com.xenocrm.exception.ResourceNotFoundException;
import com.xenocrm.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * UserService — Handles user creation and retrieval.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto createUser(UserCreateRequestDto request) {
        log.debug("Creating new user: {}", request.getUsername());

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ValidationException("Username " + request.getUsername() + " is already taken");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ValidationException("Email " + request.getEmail() + " is already in use");
        }

        UserEntity user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        
        if (request.getIsActive() != null) {
            user.setActive(request.getIsActive());
        } else {
            user.setActive(true);
        }

        Set<RoleEntity> userRoles = new HashSet<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            for (String roleName : request.getRoles()) {
                RoleEntity role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName));
                userRoles.add(role);
            }
        }
        user.setRoles(userRoles);

        UserEntity savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(UUID id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return userMapper.toResponseDto(user);
    }
}
