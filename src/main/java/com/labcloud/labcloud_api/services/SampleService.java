package com.labcloud.labcloud_api.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labcloud.labcloud_api.dto.request.SampleRequest;
import com.labcloud.labcloud_api.dto.response.SampleResponse;
import com.labcloud.labcloud_api.mapper.SampleMapper;
import com.labcloud.labcloud_api.models.Experiment;
import com.labcloud.labcloud_api.models.Sample;
import com.labcloud.labcloud_api.models.User;
import com.labcloud.labcloud_api.repositories.ExperimentRepository;
import com.labcloud.labcloud_api.repositories.SampleRepository;
import com.labcloud.labcloud_api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;
    private final ExperimentRepository experimentRepository;
    private final UserRepository userRepository;
    private final SampleMapper sampleMapper;

    public SampleResponse create(SampleRequest request, String userId) {
        log.info("Criando nova amostra: {}", request.getName());

        // Buscar experimento
        Experiment experiment = experimentRepository.findById(request.getExperimentId())
                .orElseThrow(() -> new RuntimeException("Experimento não encontrado: " + request.getExperimentId()));

        // Buscar usuário criador
        User createdBy = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + userId));

        // Converter para entidade
        Sample sample = sampleMapper.toEntity(request);
        sample.updateTenantId(experiment.getTenantId());
        sample.setExperiment(experiment);
        sample.setCreatedBy(createdBy);

        // Salvar
        Sample saved = sampleRepository.save(sample);
        log.info("Amostra criada com sucesso: {} - {}", saved.getId(), saved.getName());

        // Retornar resposta
        return sampleMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public SampleResponse findById(String id) {
        log.info("Buscando amostra por ID: {}", id);

        Sample sample = sampleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Amostra não encontrada com ID: " + id));

        return sampleMapper.toResponse(sample);
    }

    @Transactional(readOnly = true)
    public SampleResponse findByIdWithDetails(String id) {
        log.info("Buscando amostra com detalhes por ID: {}", id);

        Sample sample = sampleRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Amostra não encontrada com ID: " + id));

        return sampleMapper.toResponse(sample);
    }

    @Transactional(readOnly = true)
    public List<SampleResponse> findByTenantId(String tenantId) {
        log.info("Buscando amostras do tenant: {}", tenantId);

        return sampleRepository.findByTenantId(tenantId).stream()
                .map(sampleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<SampleResponse> findByTenantIdPaged(String tenantId, Pageable pageable) {
        log.info("Buscando amostras do tenant com paginação: {}", tenantId);

        return sampleRepository.findByTenantId(tenantId, pageable)
                .map(sampleMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<SampleResponse> findByExperiment(String experimentId) {
        log.info("Buscando amostras do experimento: {}", experimentId);

        return sampleRepository.findByExperimentId(experimentId).stream()
                .map(sampleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<SampleResponse> findByExperimentPaged(String experimentId, Pageable pageable) {
        log.info("Buscando amostras do experimento com paginação: {}", experimentId);

        return sampleRepository.findByExperimentId(experimentId, pageable)
                .map(sampleMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<SampleResponse> findByType(String tenantId, String type) {
        log.info("Buscando amostras do tipo: {} no tenant: {}", type, tenantId);

        return sampleRepository.findByTenantIdAndType(tenantId, type).stream()
                .map(sampleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SampleResponse update(String id, SampleRequest request) {
        log.info("Atualizando amostra: {}", id);

        // 1. Buscar amostra existente
        Sample sample = sampleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Amostra não encontrada com ID: " + id));

        // 2. Se experimentId mudou, atualizar
        if (request.getExperimentId() != null &&
                !sample.getExperiment().getId().equals(request.getExperimentId())) {
            Experiment experiment = experimentRepository.findById(request.getExperimentId())
                    .orElseThrow(
                            () -> new RuntimeException("Experimento não encontrado: " + request.getExperimentId()));
            sample.setExperiment(experiment);
            sample.updateTenantId(experiment.getTenantId());
        }

        // 3. Atualizar dados
        sampleMapper.updateEntity(request, sample);

        // 4. Salvar
        Sample updated = sampleRepository.save(sample);
        log.info("Amostra atualizada: {}", updated.getId());

        // 5. Retornar resposta
        return sampleMapper.toResponse(updated);
    }

    @Transactional
    public void delete(String id) {
        log.info("Deletando amostra: {}", id);

        if (!sampleRepository.existsById(id)) {
            throw new RuntimeException("Amostra não encontrada com ID: " + id);
        }

        sampleRepository.deleteById(id);
        log.info("Amostra deletada: {}", id);
    }
}
