package com.labcloud.labcloud_api.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultRequest {

    @NotBlank(message = "Parâmetro é obrigatório")
    @Size(min = 2, max = 100, message = "\"Parâmetro deve ter entre 2 e 100 caracteres\"")
    private String parameter;

    @NotBlank(message = "Valor é obrigatório")
    private String value;

    @Size(max = 20, message = "Unidade deve ter no máximo 20 caracteres")
    private String unit;

    private LocalDateTime measurementDate;

    @Size(max = 100, message = "Instrumento deve ter no máximo 100 caracteres")
    private String instrument;

    @Size(max = 255, message = "Método deve ter no máximo 255 caracteres")
    private String method;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String observations;

    private Boolean isValid = true;

    @Size(max = 500, message = "Controle de qualidade deve ter no máximo 500 caracteres")
    private String qualityControl;

    @NotNull(message = "ID da amostra é obrigatório")
    private String sampleId;

}
