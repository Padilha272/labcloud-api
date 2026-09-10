package com.labcloud.labcloud_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import com.labcloud.labcloud_api.dto.request.SampleRequest;
import com.labcloud.labcloud_api.dto.response.SampleResponse;
import com.labcloud.labcloud_api.services.SampleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/samples")
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    private static final String CURRENT_USER_ID = "user-id-temporario";

    @PostMapping
    public ResponseEntity<SampleResponse> create(@Valid @RequestBody SampleRequest request) {
        log.info("POST /api/samples - Criando amostra: {}", request.getName());
        SampleResponse response = sampleService.create(request, CURRENT_USER_ID);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SampleResponse> findById(@PathVariable String id) {
        log.info("GET /api/samples/{} - Buscando amostra", id);
        SampleResponse response = sampleService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<SampleResponse> findByIdWithDetails(@PathVariable String id) {
        log.info("GET /api/samples/{}/details - Buscando amostra com detalhes", id);
        SampleResponse response = sampleService.findByIdWithDetails(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<SampleResponse>> findByTenantId(@PathVariable String tenantId) {
        log.info("GET /api/samples/tenant/{} - Buscando amostras do tenant", tenantId);
        List<SampleResponse> responses = sampleService.findByTenantId(tenantId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/tenant/{tenantId}/paged")
    public ResponseEntity<Page<SampleResponse>> findByTenantIdPaged(
            @PathVariable String tenantId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/samples/tenant/{}/paged - Buscando amostras com paginação", tenantId);
        Page<SampleResponse> responses = sampleService.findByTenantIdPaged(tenantId, pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/experiment/{experimentId}")
    public ResponseEntity<List<SampleResponse>> findByExperiment(@PathVariable String experimentId) {
        log.info("GET /api/samples/experiment/{} - Buscando amostras do experimento", experimentId);
        List<SampleResponse> responses = sampleService.findByExperiment(experimentId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/experiment/{experimentId}/paged")
    public ResponseEntity<Page<SampleResponse>> findByExperimentPaged(
            @PathVariable String experimentId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/samples/experiment/{}/paged - Buscando amostras com paginação", experimentId);
        Page<SampleResponse> responses = sampleService.findByExperimentPaged(experimentId, pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/tenant/{tenantId}/type/{type}")
    public ResponseEntity<List<SampleResponse>> findByType(
            @PathVariable String tenantId,
            @PathVariable String type) {
        log.info("GET /api/samples/tenant/{}/type/{} - Buscando amostras por tipo", tenantId, type);
        List<SampleResponse> responses = sampleService.findByType(tenantId, type);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SampleResponse> update(
            @PathVariable String id,
            @Valid @RequestBody SampleRequest request) {
        log.info("PUT /api/samples/{} - Atualizando amostra", id);
        SampleResponse response = sampleService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("DELETE /api/samples/{} - Deletando amostra", id);
        sampleService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
