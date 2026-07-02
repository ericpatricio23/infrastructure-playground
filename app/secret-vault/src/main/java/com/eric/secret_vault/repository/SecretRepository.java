package com.eric.secret_vault.repository;

import com.eric.secret_vault.entity.Secret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecretRepository extends JpaRepository<Secret, Long> {
    List<Secret> findByUserId(Long userId);
    Optional<Secret> findByIdAndUserId(Long id, Long userId);
}
