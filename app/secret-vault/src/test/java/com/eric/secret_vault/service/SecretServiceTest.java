package com.eric.secret_vault.service;

import com.eric.secret_vault.dto.SecretRequest;
import com.eric.secret_vault.dto.SecretResponse;
import com.eric.secret_vault.entity.Secret;
import com.eric.secret_vault.entity.User;
import com.eric.secret_vault.exception.ResourceNotFoundException;
import com.eric.secret_vault.repository.SecretRepository;
import com.eric.secret_vault.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecretServiceTest {

    @Mock
    private SecretRepository secretRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private SecretService secretService;

    private User user;
    private Secret secret;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("eric").build();
        secret = Secret.builder()
                .id(10L)
                .name("api-key")
                .encryptedValue("valor-criptografado")
                .user(user)
                .build();
    }

    @Test
    void create_comUsuarioValido_deveCriarSegredo() {
        SecretRequest request = new SecretRequest();
        request.setName("api-key");
        request.setValue("valor-em-texto-puro");

        when(userRepository.findByUsername("eric")).thenReturn(Optional.of(user));
        when(encryptionService.encrypt("valor-em-texto-puro")).thenReturn("valor-criptografado");

        SecretResponse response = secretService.create("eric", request);

        assertEquals("api-key", response.getName());
        verify(secretRepository).save(any(Secret.class));
    }

    @Test
    void create_comUsuarioInexistente_deveLancarExcecao() {
        SecretRequest request = new SecretRequest();
        request.setName("api-key");
        request.setValue("valor");
        when(userRepository.findByUsername("fantasma")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> secretService.create("fantasma", request));
    }

    @Test
    void findById_comSegredoExistente_deveRetornarSegredoDescriptografado() {
        when(userRepository.findByUsername("eric")).thenReturn(Optional.of(user));
        when(secretRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.of(secret));
        when(encryptionService.decrypt("valor-criptografado")).thenReturn("valor-original");

        SecretResponse response = secretService.findById("eric", 10L);

        assertEquals("valor-original", response.getValue());
    }

    @Test
    void findById_comSegredoInexistente_deveLancarResourceNotFoundException() {
        when(userRepository.findByUsername("eric")).thenReturn(Optional.of(user));
        when(secretRepository.findByIdAndUserId(999L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> secretService.findById("eric", 999L));
    }

    @Test
    void findById_comSegredoDeOutroUsuario_deveLancarResourceNotFoundException() {
        // findByIdAndUserId já filtra pelo dono; se o segredo for de outro usuário,
        // o repository simplesmente não encontra nada.
        when(userRepository.findByUsername("eric")).thenReturn(Optional.of(user));
        when(secretRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> secretService.findById("eric", 10L));
    }

    @Test
    void findAll_deveRetornarListaDeSegredosDoUsuario() {
        when(userRepository.findByUsername("eric")).thenReturn(Optional.of(user));
        when(secretRepository.findByUserId(1L)).thenReturn(List.of(secret));
        when(encryptionService.decrypt("valor-criptografado")).thenReturn("valor-original");

        List<SecretResponse> response = secretService.findAll("eric");

        assertEquals(1, response.size());
        assertEquals("api-key", response.get(0).getName());
    }

    @Test
    void delete_comSegredoExistente_deveChamarRepositoryDelete() {
        when(userRepository.findByUsername("eric")).thenReturn(Optional.of(user));
        when(secretRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.of(secret));

        secretService.delete("eric", 10L);

        verify(secretRepository).delete(secret);
    }

    @Test
    void delete_comSegredoInexistente_deveLancarResourceNotFoundException() {
        when(userRepository.findByUsername("eric")).thenReturn(Optional.of(user));
        when(secretRepository.findByIdAndUserId(999L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> secretService.delete("eric", 999L));
    }
}
