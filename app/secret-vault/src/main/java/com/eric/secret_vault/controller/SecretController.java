package com.eric.secret_vault.controller;

import com.eric.secret_vault.dto.SecretRequest;
import com.eric.secret_vault.dto.SecretResponse;
import com.eric.secret_vault.service.SecretService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/secrets")
@RequiredArgsConstructor
public class SecretController {

    private final SecretService secretService;

    @PostMapping
    public ResponseEntity<SecretResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody SecretRequest request) {
        return ResponseEntity.ok(secretService.create(userDetails.getUsername(), request));
    }

    @GetMapping
    public ResponseEntity<List<SecretResponse>> findAll(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(secretService.findAll(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecretResponse> findById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(secretService.findById(userDetails.getUsername(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SecretResponse> update(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody SecretRequest request) {
        return ResponseEntity.ok(secretService.update(userDetails.getUsername(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        secretService.delete(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}
