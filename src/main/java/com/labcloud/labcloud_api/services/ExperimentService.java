package com.labcloud.labcloud_api.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.labcloud.labcloud_api.dto.request.ExperimentRequest;
import com.labcloud.labcloud_api.dto.response.ExperimentResponse;
import com.labcloud.labcloud_api.enums.ExperimentStatus;
import com.labcloud.labcloud_api.mapper.ExperimentMapper;
import com.labcloud.labcloud_api.models.Experiment;
import com.labcloud.labcloud_api.models.Laboratory;
import com.labcloud.labcloud_api.models.User;
import com.labcloud.labcloud_api.repositories.ExperimentRepository;
import com.labcloud.labcloud_api.repositories.LaboratoryRepository;
import com.labcloud.labcloud_api.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExperimentService {
    private final ExperimentRepository experimentRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final UserRepository userRepository;
    private final ExperimentMapper experimentMapper;

    public ExperimentResponse create(ExperimentRequest request, String userId) {
        log.info("Criando novo experimento: {}", request.getName());

        // Buscar laboratório
        Laboratory laboratory = laboratoryRepository.findById(request.getLaboratoryId())
                .orElseThrow(() -> new RuntimeException("Laboratório não encontrado: " + request.getLaboratoryId()));

        // Buscar usuário criador
        User createdBy = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + userId));

        // Converter para entidade
        Experiment experiment = experimentMapper.toEntity(request);
        experiment.updateTenantId(laboratory.getTenantId());
        experiment.setLaboratory(laboratory);
        experiment.setCreatedBy(createdBy);

        // Se status for ACTIVE, definir startDate
        if (experiment.getStatus() == ExperimentStatus.ACTIVE) {
            experiment.activate();
        }

        // Salvar
        Experiment saved = experimentRepository.save(experiment);
        log.info("Experimento criado com sucesso: {} - {}", saved.getId(), saved.getName());

        // Retornar resposta
        return experimentMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ExperimentResponse findById(String id) {
        log.info("Buscando experimento por ID: {}", id);

        Experiment experiment = experimentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Experimento não encontrado com ID: " + id));

        return experimentMapper.toResponse(experiment);
    }

    @Transactional(readOnly = true)
    public ExperimentResponse findByIdWithDetails(String id) {
        log.info("Buscando experimento com detalhes por ID: {}", id);

        Experiment experiment = experimentRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Experimento não encontrado com ID: " + id));

        return experimentMapper.toResponse(experiment);
    }

    @Transactional(readOnly = true)
    public List<ExperimentResponse> findByTenantId(String tenantId) {
        log.info("Buscando experimentos do tenant: {}", tenantId);

        return experimentRepository.findByTenantId(tenantId).stream()
                .map(experimentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ExperimentResponse> findByTenantIdPaged(String tenantId, Pageable pageable) {
        log.info("Buscando experimentos do tenant com paginação: {}", tenantId);

        return experimentRepository.findByTenantId(tenantId, pageable)
                .map(experimentMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ExperimentResponse> findByLaboratory(String laboratoryId) {
        log.info("Buscando experimentos do laboratório: {}", laboratoryId);

        return experimentRepository.findByLaboratoryId(laboratoryId).stream()
                .map(experimentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExperimentResponse> findByStatus(String tenantId, ExperimentStatus status) {
        log.info("Buscando experimentos com status: {} no tenant: {}", status, tenantId);

        return experimentRepository.findByTenantIdAndStatus(tenantId, status).stream()
                .map(experimentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExperimentResponse> findActive(String tenantId) {
        log.info("Buscando experimentos ativos no tenant: {}", tenantId);

        return experimentRepository.findActiveExperiments(tenantId).stream()
                .map(experimentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExperimentResponse update(String id, ExperimentRequest request) {
        log.info("Atualizando experimento: {}", id);

        // 1. Buscar experimento existente
        Experiment experiment = experimentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Experimento não encontrado com ID: " + id));

        // 2. Se laboratoryId mudou, atualizar
        if (request.getLaboratoryId() != null &&
                !experiment.getLaboratory().getId().equals(request.getLaboratoryId())) {
            Laboratory laboratory = laboratoryRepository.findById(request.getLaboratoryId())
                    .orElseThrow(
                            () -> new RuntimeException("Laboratório não encontrado: " + request.getLaboratoryId()));
            experiment.setLaboratory(laboratory);
            experiment.updateTenantId((laboratory.getTenantId()));
        }

        // 3. Atualizar dados
        experimentMapper.updateEntity(request, experiment);

        // 4. Se status for ACTIVE e não tiver startDate, definir
        if (experiment.getStatus() == ExperimentStatus.ACTIVE && experiment.getStartDate() == null) {
            experiment.activate();
        }

        // 5. Salvar
        Experiment updated = experimentRepository.save(experiment);
        log.info("Experimento atualizado: {}", updated.getId());

        // 6. Retornar resposta
        return experimentMapper.toResponse(updated);
    }

    @Transactional
    public ExperimentResponse complete(String id) {
        log.info("Completando experimento: {}", id);

        Experiment experiment = experimentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Experimento não encontrado com ID: " + id));

        experiment.complete();
        Experiment updated = experimentRepository.save(experiment);

        log.info("Experimento completado: {}", id);
        return experimentMapper.toResponse(updated);
    }

    @Transactional
    public ExperimentResponse cancel(String id) {
        log.info("Cancelando experimento: {}", id);

        Experiment experiment = experimentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Experimento não encontrado com ID: " + id));

        experiment.cancel();
        Experiment updated = experimentRepository.save(experiment);

        log.info("Experimento cancelado: {}", id);
        return experimentMapper.toResponse(updated);
    }

    @Transactional
    public ExperimentResponse activate(String id) {
        log.info("Ativando experimento: {}", id);

        Experiment experiment = experimentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Experimento não encontrado com ID: " + id));

        experiment.activate();
        Experiment updated = experimentRepository.save(experiment);

        log.info("Experimento ativado: {}", id);
        return experimentMapper.toResponse(updated);
    }

    @Transactional
    public void delete(String id) {
        log.info("Deletando experimento: {}", id);

        if (!experimentRepository.existsById(id)) {
            throw new RuntimeException("Experimento não encontrado com ID: " + id);
        }

        experimentRepository.deleteById(id);
        log.info("Experimento deletado: {}", id);
    }

}
