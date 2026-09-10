package com.labcloud.labcloud_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.labcloud.labcloud_api.dto.request.LaboratoryRequest;
import com.labcloud.labcloud_api.dto.response.LaboratoryResponse;
import com.labcloud.labcloud_api.services.LaboratoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/laboratories")
@RequiredArgsConstructor
public class LaboratoryController {

    private final LaboratoryService laboratoryService;

    @PostMapping
    public ResponseEntity<LaboratoryResponse> create(@Valid @RequestBody LaboratoryRequest request) {
        log.info("POST /api/laboratories - Criando {}", request.getName());
        LaboratoryResponse response = laboratoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaboratoryResponse> findById(@PathVariable String id) {
        log.info("GET /api/laboratories/{} - Buscando laboratório", id);
        LaboratoryResponse response = laboratoryService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<LaboratoryResponse> findByTenantId(@PathVariable String tenantId) {
        log.info("GET /api/tenant/{} - Buscando laboratório por tenant", tenantId);
        LaboratoryResponse response = laboratoryService.findByTenantId(tenantId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<LaboratoryResponse>> findAll() {
        log.info("GET /api/laboratories - Buscando todos os laboratórios");
        List<LaboratoryResponse> responses = laboratoryService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LaboratoryResponse> update(
            @PathVariable String id,
            @Valid @RequestBody LaboratoryRequest request) {
        log.info("PUT /api/laboratories/{} - Atualizando laboratório", id);
        LaboratoryResponse response = laboratoryService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LaboratoryResponse> delete(@PathVariable String id) {
        log.info("DELETE /api/laboratories/{} - Deletando laboratório", id);
        laboratoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
