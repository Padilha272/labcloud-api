package com.labcloud.labcloud_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponse {
    private String id;
    private String parameter;
    private String value;
    private String unit;
    private LocalDateTime measurementDate;
    private String instrument;
    private String method;
    private String observations;
    private Boolean isValid;
    private String qualityControl;
    private String tenantId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Dados da amostra
    private String sampleId;
    private String sampleName;

    // Dados do criador
    private String createdBy;
    private String createdByName;
}