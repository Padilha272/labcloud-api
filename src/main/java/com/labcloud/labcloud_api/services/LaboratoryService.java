package com.labcloud.labcloud_api.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labcloud.labcloud_api.dto.response.LaboratoryResponse;
import com.labcloud.labcloud_api.exception.DuplicateResourceException;
import com.labcloud.labcloud_api.exception.ResourceNotFoundException;
import com.labcloud.labcloud_api.dto.request.LaboratoryRequest;
import com.labcloud.labcloud_api.mapper.LaboratoryMapper;
import com.labcloud.labcloud_api.models.Laboratory;
import com.labcloud.labcloud_api.repositories.LaboratoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaboratoryService {

    private final LaboratoryRepository laboratoryRepository;
    private final LaboratoryMapper laboratoryMapper;

    @Transactional
    public LaboratoryResponse create(LaboratoryRequest request) {
        log.info("Criando novo laboratório: {}", request.getName());

        // Gerar tenantId único
        String tenantId = generateTenantId(request.getName());

        // Verificar se tenantId já existe
        if (laboratoryRepository.existsByTenantId(tenantId)) {
            throw new DuplicateResourceException("Laboratório", "tenantId", tenantId);
        }

        // Converter para entidade
        Laboratory laboratory = laboratoryMapper.toEntity(request);
        laboratory.updateTenantId(tenantId);
        laboratory.setActive(true);

        // Salvar
        Laboratory saved = laboratoryRepository.save(laboratory);
        log.info("Laboratório criado com sucesso: {} - {}", saved.getId(), saved.getName());

        // Retornar Resposta
        return laboratoryMapper.toResponse(saved);

    }

    @Transactional(readOnly = true)
    public LaboratoryResponse findById(String id) {
        log.info("Buscando pelo ID: {}", id);

        Laboratory laboratory = laboratoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Laboratório", "ID", id));

        return laboratoryMapper.toResponse(laboratory);
    }

    @Transactional(readOnly = true)
    public LaboratoryResponse findByTenantId(String tenantId) {
        log.info("Buscando pelo TenantId: {}", tenantId);

        Laboratory laboratory = laboratoryRepository.findByTenantId(tenantId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Laboratório", "tenantId", tenantId));
        return laboratoryMapper.toResponse(laboratory);

    }

    @Transactional(readOnly = true)
    public List<LaboratoryResponse> findAll() {
        log.info("Buscando todos os laboratorios");

        return laboratoryRepository.findAll().stream()
                .map(laboratoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LaboratoryResponse> findActive() {
        log.info("Buscando laboratórios ativos");

        return laboratoryRepository.findByActiveTrue().stream()
                .map(laboratoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LaboratoryResponse update(String id, LaboratoryRequest request) {
        log.info("Atualizando laboratório: {}", id);

        // Buscar laboratório existente
        Laboratory laboratory = laboratoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Laboratório", "ID", id));

        // Atualizar dados
        laboratoryMapper.updateEntity(request, laboratory);

        // Salvar
        Laboratory updated = laboratoryRepository.save(laboratory);
        log.info("Laboratório atualizado {}", updated.getId());

        // Retornar resposta
        return laboratoryMapper.toResponse(updated);

    }

    @Transactional
    public void delete(String id) {
        log.info("Deletando laboratório: {}", id);

        Laboratory laboratory = laboratoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Laboratório", "ID", id));

        laboratory.setActive(false);
        laboratoryRepository.save(laboratory);

        log.info("Laboratório desativado: {}", id);

    }

    @Transactional
    public void hardDelete(String id) {
        log.info("Removendo permanentemente laboratório: {}", id);

        if (!laboratoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Laboratório", "ID", id);
        }

        laboratoryRepository.deleteById(id);
        log.info("Laboratório removido permanentemente: {}", id);

    }

    private String generateTenantId(String name) {

        String normalized = java.text.Normalizer
                .normalize(name, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        String base = normalized.toLowerCase()
                .replaceAll(" ", "-")
                .replaceAll("[^a-z0-9-]", "");

        String tenantId = base;
        int counter = 1;

        // Garantir que o tenantId é único
        while (laboratoryRepository.existsByTenantId(tenantId)) {
            tenantId = base + "-" + counter;
            counter++;
        }

        return tenantId;
    }

}
