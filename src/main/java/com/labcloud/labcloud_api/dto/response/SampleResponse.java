package com.labcloud.labcloud_api.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SampleResponse {

    private String id;
    private String name;
    private String type;
    private LocalDateTime collectionDate;
    private String collectionMethod;
    private Double quantity;
    private String unit;
    private String storageConditions;
    private String location;
    private String notes;
    private String tenantId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Dados do experimento
    private String experimentId;
    private String experimentName;

    // Dados do criador
    private String createdBy;
    private String createdByName;

    // Estatísticas
    private Integer totalResults;

}
