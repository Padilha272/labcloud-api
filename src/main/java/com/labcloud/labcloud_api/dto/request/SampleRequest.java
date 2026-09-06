package com.labcloud.labcloud_api.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SampleRequest {

    @NotBlank(message = "Nome da amostra é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter 3 a 100 caracteres")
    private String name;

    @NotBlank(message = "O tipo da amostra é obrigatório")
    @Size(max = 50, message = "Nome deve ter no máximo 50 caracteres")
    private String type;

    private LocalDateTime collectionDate;

    @Size(max = 100, message = "Método de coleta deve ter no máximo 100 caracteres")
    private String collectionMethod;

    @Positive(message = "A quantidade deve ser positiva")
    private Double quantity;

    @Size(max = 20, message = "Unidade deve ter no máximo 20 caracteres")
    private String unit;

    @Size(max = 255, message = "Condições de armazenamento devem ter no máximo 255 caracteres")
    private String storageConditions;

    @Size(max = 100, message = "As localização deve ter no máximo 100 caracteres")
    private String location;

    @Size(max = 500, message = "Notas devem ter no máximo 500 caracteres")
    private String notes;

    @NotNull(message = "ID do experimento é obrigatório")
    private String experimentId;

}
