package com.labcloud.labcloud_api.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.labcloud.labcloud_api.dto.request.AuthRequest;
import com.labcloud.labcloud_api.dto.request.RegisterRequest;
import com.labcloud.labcloud_api.dto.response.AuthResponse;
import com.labcloud.labcloud_api.enums.UserRole;
import com.labcloud.labcloud_api.models.Laboratory;
import com.labcloud.labcloud_api.models.User;
import com.labcloud.labcloud_api.repositories.LaboratoryRepository;
import com.labcloud.labcloud_api.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse login(AuthRequest request) {
        log.info("Tentativa de login: {}", request.getEmail());

        // 1. Autenticar
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        // 2. Buscar usuário
        User user = userRepository.findByEmailWithLaboratory(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 3. Atualizar último login
        user.updateLastLogin();
        userRepository.save(user);

        // 4. Gerar token
        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getTenantId(),
                user.getRole().name());

        log.info("Login realizado com sucesso: {}", user.getEmail());

        // 5. Retornar resposta
        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .tenantId(user.getTenantId())
                .laboratoryName(user.getLaboratory() != null ? user.getLaboratory().getName() : null)
                .build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registrando novo laboratório e usuário admin: {}", request.getEmail());

        // 1. Verificar se email já existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + request.getEmail());
        }

        // 2. Criar laboratório
        Laboratory laboratory = Laboratory.builder()
                .name(request.getLaboratoryName())
                .tenantId(generateTenantId(request.getLaboratoryName()))
                .active(true)
                .build();

        Laboratory savedLab = laboratoryRepository.save(laboratory);

        // 3. Criar usuário admin
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ADMIN)
                .active(true)
                .laboratory(savedLab)
                .build();
        user.updateTenantId(savedLab.getTenantId());

        User savedUser = userRepository.save(user);

        // 4. Gerar token
        String token = jwtService.generateToken(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getTenantId(),
                savedUser.getRole().name());

        log.info("Registro realizado com sucesso: {} - Lab: {}", savedUser.getEmail(), savedLab.getName());

        // 5. Retornar resposta
        return AuthResponse.builder()
                .token(token)
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .tenantId(savedUser.getTenantId())
                .laboratoryName(savedLab.getName())
                .build();
    }

    private String generateTenantId(String name) {
        String base = name.toLowerCase()
                .replaceAll(" ", "-")
                .replaceAll("[^a-z0-9-]", "");

        String tenantId = base;
        int counter = 1;

        while (laboratoryRepository.existsByTenantId(tenantId)) {
            tenantId = base + "-" + counter;
            counter++;
        }

        return tenantId;
    }
}
