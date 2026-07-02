package com.eric.secret_vault.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username obrigatorio")
    @Size(min = 3, max = 50, message = "Username deve ter entre 3 e 50 caracteres")
    private String username;

    @NotBlank(message = "Email obrigatorio")
    @Email(message = "Email invalido")
    private String email;

    @NotBlank(message = "Senha obrigatoria")
    @Size(min = 6, message = "Senha deve ter no minimo 6 caracteres")
    private String password;
}
