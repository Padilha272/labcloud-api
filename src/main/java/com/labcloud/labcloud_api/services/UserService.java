package com.labcloud.labcloud_api.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.labcloud.labcloud_api.dto.request.UserRequest;
import com.labcloud.labcloud_api.dto.response.UserResponse;
import com.labcloud.labcloud_api.enums.UserRole;
import com.labcloud.labcloud_api.mapper.UserMapper;
import com.labcloud.labcloud_api.models.Laboratory;
import com.labcloud.labcloud_api.models.User;
import com.labcloud.labcloud_api.repositories.LaboratoryRepository;
import com.labcloud.labcloud_api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LaboratoryRepository laboratoryRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse create(UserRequest request) {
        log.info("Criando novo usuário: {}", request.getEmail());

        // Verificar se email já existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("O email já cadastrado" + request.getEmail());
        }

        // Buscar laboratório
        Laboratory laboratory = laboratoryRepository.findById(request.getLaboratoryId())
                .orElseThrow(() -> new RuntimeException("Laboratório não encontrado" + request.getLaboratoryId()));

        // Converter para entidade
        User user = userMapper.toEntity(request);
        user.updateTenantId(laboratory.getTenantId());
        user.setLaboratory(laboratory);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);

        if (user.getRole() == null) {
            user.setRole(UserRole.RESEARCHER);
        }

        // Salvar
        User saved = userRepository.save(user);
        log.info("Usuário criado com sucesso: {} - {}", saved.getId(), saved.getEmail());

        // Retornar resposta
        return userMapper.toResponse(saved);

    }

    @Transactional(readOnly = true)
    public UserResponse findById(String id) {
        log.info("Buscando pelo ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
        return userMapper.toResponse(user);

    }

    @Transactional(readOnly = true)
    public UserResponse findByEmail(String email) {
        log.info("Buscando por email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrando pelo email: " + email));
        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        log.info("Buscando todos os usuários");

        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findByLaboratory(String laboratoryId) {
        log.info("Buscando usuários do laboratório: {}", laboratoryId);

        // Verificar se o laboratório existe
        if (!laboratoryRepository.existsById(laboratoryId)) {
            throw new RuntimeException("Laboratório não encontrado: " + laboratoryId);
        }

        return userRepository.findByLaboratoryId(laboratoryId).stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findByLaboratoryAndRole(String laboratoryId, UserRole role) {
        log.info("Buscando usuários do laboratório {} com role: {}", laboratoryId, role);

        return userRepository.findByLaboratoryIdAndRole(laboratoryId, role).stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse update(String id, UserRequest request) {
        log.info("Atualizando usuário: {}", id);

        // 1. Buscar usuário existente
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        // 2. Se email mudou, verificar se já existe
        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + request.getEmail());
        }

        // 3. Se laboratoryId mudou, atualizar
        if (request.getLaboratoryId() != null &&
                !user.getLaboratory().getId().equals(request.getLaboratoryId())) {
            Laboratory laboratory = laboratoryRepository.findById(request.getLaboratoryId())
                    .orElseThrow(
                            () -> new RuntimeException("Laboratório não encontrado: " + request.getLaboratoryId()));
            user.setLaboratory(laboratory);
            user.updateTenantId(laboratory.getTenantId());
        }

        // 4. Atualizar dados
        userMapper.updateEntity(request, user);

        // 5. Se senha veio, criptografar
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // 6. Salvar
        User updated = userRepository.save(user);
        log.info("Usuário atualizado: {}", updated.getId());

        // 7. Retornar resposta
        return userMapper.toResponse(updated);
    }

    @Transactional
    public void delete(String id) {
        log.info("Deletando usuário: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        user.setActive(false);
        userRepository.save(user);

        log.info("Usuário desativado: {}", id);
    }

    @Transactional
    public void hardDelete(String id) {
        log.info("Removendo permanentemente usuário: {}", id);

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }

        userRepository.deleteById(id);
        log.info("Usuário removido permanentemente: {}", id);
    }

    @Transactional
    public void updateLastLogin(String email) {
        log.info("Atualizando último login do usuário: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + email));

        user.updateLastLogin();
        userRepository.save(user);
    }

}
