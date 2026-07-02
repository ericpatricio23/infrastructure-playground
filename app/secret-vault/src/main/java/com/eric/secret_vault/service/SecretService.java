package com.eric.secret_vault.service;

import com.eric.secret_vault.dto.SecretRequest;
import com.eric.secret_vault.dto.SecretResponse;
import com.eric.secret_vault.entity.Secret;
import com.eric.secret_vault.entity.User;
import com.eric.secret_vault.repository.SecretRepository;
import com.eric.secret_vault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecretService {

    private final SecretRepository secretRepository;
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado"));
    }

    public SecretResponse create(String username, SecretRequest request) {
        User user = getUser(username);
        Secret secret = Secret.builder()
                .name(request.getName())
                .encryptedValue(encryptionService.encrypt(request.getValue()))
                .user(user)
                .build();
        secretRepository.save(secret);
        return toResponse(secret, request.getValue());
    }

    public List<SecretResponse> findAll(String username) {
        User user = getUser(username);
        return secretRepository.findByUserId(user.getId())
                .stream()
                .map(s -> toResponse(s, encryptionService.decrypt(s.getEncryptedValue())))
                .collect(Collectors.toList());
    }

    public SecretResponse findById(String username, Long id) {
        User user = getUser(username);
        Secret secret = secretRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Segredo nao encontrado"));
        return toResponse(secret, encryptionService.decrypt(secret.getEncryptedValue()));
    }

    public SecretResponse update(String username, Long id, SecretRequest request) {
        User user = getUser(username);
        Secret secret = secretRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Segredo nao encontrado"));
        secret.setName(request.getName());
        secret.setEncryptedValue(encryptionService.encrypt(request.getValue()));
        secretRepository.save(secret);
        return toResponse(secret, request.getValue());
    }

    public void delete(String username, Long id) {
        User user = getUser(username);
        Secret secret = secretRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Segredo nao encontrado"));
        secretRepository.delete(secret);
    }

    private SecretResponse toResponse(Secret secret, String decryptedValue) {
        return new SecretResponse(
                secret.getId(),
                secret.getName(),
                decryptedValue,
                secret.getCreatedAt(),
                secret.getUpdatedAt()
        );
    }
}
