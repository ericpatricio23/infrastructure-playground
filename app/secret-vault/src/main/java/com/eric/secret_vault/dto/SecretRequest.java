package com.eric.secret_vault.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SecretRequest {

    @NotBlank(message = "Nome obrigatorio")
    private String name;

    @NotBlank(message = "Valor obrigatorio")
    private String value;
}
