package com.labcloud.labcloud_api.mapper;

import org.springframework.stereotype.Component;

import com.labcloud.labcloud_api.dto.request.ExperimentRequest;
import com.labcloud.labcloud_api.dto.response.ExperimentResponse;
import com.labcloud.labcloud_api.models.Experiment;

@Component
public class ExperimentMapper {

    public Experiment toEntity(ExperimentRequest request) {
        if (request == null)
            return null;

        Experiment experiment = new Experiment();
        experiment.setName(request.getName());
        experiment.setDescription(request.getDescription());
        experiment.setStatus(request.getStatus());
        experiment.setStartDate(request.getStartDate());
        experiment.setEndDate(request.getEndDate());
        experiment.setObjective(request.getObjective());
        experiment.setMethodology(request.getMethodology());

        return experiment;
    }

    public ExperimentResponse toResponse(Experiment experiment) {
        if (experiment == null)
            return null;

        return ExperimentResponse.builder()
                .id(experiment.getId())
                .tenantId(experiment.getTenantId())
                .name(experiment.getName())
                .description(experiment.getDescription())
                .status(experiment.getStatus())
                .startDate(experiment.getStartDate())
                .endDate(experiment.getEndDate())
                .objective(experiment.getObjective())
                .methodology(experiment.getMethodology())
                .createdAt(experiment.getCreatedAt())
                .updatedAt(experiment.getUpdatedAt())
                .laboratoryId(experiment.getLaboratory() != null ? experiment.getLaboratory().getId() : null)
                .laboratoryName(experiment.getLaboratory() != null ? experiment.getLaboratory().getName() : null)
                .createdBy(experiment.getCreatedBy() != null ? experiment.getCreatedBy().getId() : null)
                .createdByName(experiment.getCreatedBy() != null ? experiment.getCreatedBy().getName() : null)
                .totalSamples(experiment.getSamples() != null ? experiment.getSamples().size() : 0)
                .build();

    }

}
