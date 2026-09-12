package com.labcloud.labcloud_api.exception;

import java.util.Map;

import lombok.Getter;

/**
 * Exceção personalizada para transportar erros de validações de negócio
 * customizadas.
 * Carrega um mapa onde a chave é o nome do campo e o valor é a mensagem de
 * erro.
 */
@Getter
public class ValidationException extends RuntimeException {
    // Mapa contendo os campos inválidos e suas respectivas mensagens
    // Ex: {"email": "E-mail já cadastrado", "quantity": "Quantidade deve ser maior
    // que zero"}

    private final Map<String, String> errors;

    /**
     * Constrói a exceção passando um mapa com todos os erros acumulados na regra de
     * negócio.
     * 
     * @param errors Dicionário de erros (campo -> mensagem de erro)
     */
    public ValidationException(Map<String, String> errors) {
        super("Erro de validação");
        this.errors = errors;
    }

}
