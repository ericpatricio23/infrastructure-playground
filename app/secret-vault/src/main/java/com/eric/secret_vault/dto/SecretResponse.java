package com.eric.secret_vault.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SecretResponse {
    private Long id;
    private String name;
    private String value;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
