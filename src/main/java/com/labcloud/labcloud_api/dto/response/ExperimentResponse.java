package com.labcloud.labcloud_api.dto.response;

import java.time.LocalDateTime;

import com.labcloud.labcloud_api.enums.ExperimentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperimentResponse {

    private String id;
    public String tenantId;
    public String name;
    private String description;
    private ExperimentStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String objective;
    private String methodology;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Dados do laboratório
    private String laboratoryId;
    private String laboratoryName;

    // Dados do criador
    private String createdBy;
    private String createdByName;

    // Estatísticas
    private Integer totalSamples;
    private Integer totalResults;

}
