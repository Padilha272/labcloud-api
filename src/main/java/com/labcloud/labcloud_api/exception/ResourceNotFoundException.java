package com.labcloud.labcloud_api.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s não encontrado com %s: %s", resource, field, value));

        /*
         * resource -> Entidade do sistema (ex: "Laboratory", "Usuário")
         * field -> Atributo utilizado na usca (ex: "ID", "email")
         * value -> Valor real não encontrado (ex: "123", "joao@labcloud.com")
         */

    }
}
