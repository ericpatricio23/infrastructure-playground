package com.eric.secret_vault.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Username obrigatorio")
    private String username;

    @NotBlank(message = "Senha obrigatoria")
    private String password;
}
