package com.labcloud.labcloud_api.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labcloud.labcloud_api.dto.request.ResultRequest;
import com.labcloud.labcloud_api.dto.response.ResultResponse;
import com.labcloud.labcloud_api.exception.ResourceNotFoundException;
import com.labcloud.labcloud_api.mapper.ResultMapper;
import com.labcloud.labcloud_api.models.Result;
import com.labcloud.labcloud_api.models.Sample;
import com.labcloud.labcloud_api.models.User;
import com.labcloud.labcloud_api.repositories.ResultRepository;
import com.labcloud.labcloud_api.repositories.SampleRepository;
import com.labcloud.labcloud_api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultService {
    private final ResultRepository resultRepository;
    private final SampleRepository sampleRepository;
    private final UserRepository userRepository;
    private final ResultMapper resultMapper;

    public ResultResponse create(ResultRequest request, String userId) {
        log.info("Criando novo resultado para amostra: {}", request.getSampleId());

        // 1. Buscar amostra
        Sample sample = sampleRepository.findById(request.getSampleId())
                .orElseThrow(() -> new ResourceNotFoundException("Amostra", "ID", request.getSampleId()));

        // 2. Buscar usuário criador
        User createdBy = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "ID", userId));

        // 3. Converter para entidade
        Result result = resultMapper.toEntity(request);
        result.updateTenantId(sample.getTenantId());
        result.setSample(sample);
        result.setCreatedBy(createdBy);

        // 4. Salvar
        Result saved = resultRepository.save(result);
        log.info("Resultado criado com sucesso: {} - {}", saved.getId(), saved.getParameter());

        // 5. Retornar resposta
        return resultMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ResultResponse findById(String id) {
        log.info("Buscando resultado por ID: {}", id);

        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resultado", "ID", id));

        return resultMapper.toResponse(result);
    }

    @Transactional(readOnly = true)
    public ResultResponse findByIdWithDetails(String id) {
        log.info("Buscando resultado com detalhes por ID: {}", id);

        Result result = resultRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resultado", "ID", id));

        return resultMapper.toResponse(result);
    }

    @Transactional(readOnly = true)
    public List<ResultResponse> findByTenantId(String tenantId) {
        log.info("Buscando resultados do tenant: {}", tenantId);

        return resultRepository.findByTenantId(tenantId).stream()
                .map(resultMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ResultResponse> findByTenantIdPaged(String tenantId, Pageable pageable) {
        log.info("Buscando resultados do tenant com paginação: {}", tenantId);

        return resultRepository.findByTenantId(tenantId, pageable)
                .map(resultMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ResultResponse> findBySample(String sampleId) {
        log.info("Buscando resultados da amostra: {}", sampleId);

        return resultRepository.findBySampleId(sampleId).stream()
                .map(resultMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ResultResponse> findBySamplePaged(String sampleId, Pageable pageable) {
        log.info("Buscando resultados da amostra com paginação: {}", sampleId);

        return resultRepository.findBySampleId(sampleId, pageable)
                .map(resultMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ResultResponse> findByExperiment(String experimentId) {
        log.info("Buscando resultados do experimento: {}", experimentId);

        return resultRepository.findByExperimentId(experimentId).stream()
                .map(resultMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResultResponse> findByParameter(String tenantId, String parameter) {
        log.info("Buscando resultados do parâmetro: {} no tenant: {}", parameter, tenantId);

        return resultRepository.findByTenantIdAndParameter(tenantId, parameter).stream()
                .map(resultMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResultResponse> findValidResults(String tenantId) {
        log.info("Buscando resultados válidos no tenant: {}", tenantId);

        return resultRepository.findByTenantIdAndIsValidTrue(tenantId).stream()
                .map(resultMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResultResponse update(String id, ResultRequest request) {
        log.info("Atualizando resultado: {}", id);

        // 1. Buscar resultado existente
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resultado", "ID", id));

        // 2. Se sampleId mudou, atualizar
        if (request.getSampleId() != null &&
                !result.getSample().getId().equals(request.getSampleId())) {
            Sample sample = sampleRepository.findById(request.getSampleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Amostra", "ID", request.getSampleId()));
            result.setSample(sample);
            result.updateTenantId((sample.getTenantId()));
        }

        // 3. Atualizar dados
        resultMapper.updateEntity(request, result);

        // 4. Salvar
        Result updated = resultRepository.save(result);
        log.info("Resultado atualizado: {}", updated.getId());

        // 5. Retornar resposta
        return resultMapper.toResponse(updated);
    }

    @Transactional
    public void delete(String id) {
        log.info("Deletando resultado: {}", id);

        if (!resultRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resultado", "ID", id);
        }

        resultRepository.deleteById(id);
        log.info("Resultado deletado: {}", id);
    }

}
