package com.labcloud.labcloud_api.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.labcloud.labcloud_api.dto.request.UserRequest;
import com.labcloud.labcloud_api.dto.response.UserResponse;
import com.labcloud.labcloud_api.enums.UserRole;
import com.labcloud.labcloud_api.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
        log.info("POST /api/users - Criando {}", request.getEmail());
        UserResponse response = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable String id) {
        log.info("GET /api/users/{} Buscando usuário", id);
        UserResponse response = userService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> findByEmail(@PathVariable String email) {
        log.info("GET /api/users/email/{} - Buscando usuário por email", email);
        UserResponse response = userService.findByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        log.info("GET /api/users - Buscando todos os usuários");
        List<UserResponse> responses = userService.findAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/laboratorios/{laboratorioId}")
    public ResponseEntity<List<UserResponse>> findByLaboratory(@PathVariable String laboratoryId) {
        log.info("GET /api/users/laboratory/{} - Buscando os usuários do laboratório", laboratoryId);
        List<UserResponse> responses = userService.findByLaboratory(laboratoryId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/laboratory/{laboratoryId}/role/{role}")
    public ResponseEntity<List<UserResponse>> findByLaboratoryAndRole(
            @PathVariable String laboratoryId,
            @PathVariable UserRole role) {
        log.info("GET /api/users/laboratory/{}/role/{} - Buscando usuários por role", laboratoryId, role);
        List<UserResponse> responses = userService.findByLaboratoryAndRole(laboratoryId, role);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable String id,
            @Valid @RequestBody UserRequest request) {
        log.info("PUT /api/users/{} - Atualizando usuário", id);
        UserResponse response = userService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("DELETE /api/users/{} - Deletando usuário", id);
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
