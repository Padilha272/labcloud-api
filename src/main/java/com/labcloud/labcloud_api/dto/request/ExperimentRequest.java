package com.labcloud.labcloud_api.dto.request;

import com.labcloud.labcloud_api.enums.ExperimentStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExperimentRequest {

    @NotBlank(message = "Nome do experimento é obrigatório")
    @Size(min = 3, max = 255, message = "Nome deve ter no máximo 255 caracteres")
    private String name;

    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
    private String description;

    @NotNull(message = "Status é obrigatório")
    private ExperimentStatus status;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Size(max = 500, message = "Objetivo deve ter no máximo 500 caracteres")
    private String objective;

    @Size(max = 500, message = "Metodologia deve ter no máximo 500 caracteres")
    private String methodology;

    @NotNull(message = "ID do laboratório é obrigatório")
    private String laboratoryId;

}
