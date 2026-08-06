package com.eric.secret_vault.service;

import com.eric.secret_vault.dto.AuthResponse;
import com.eric.secret_vault.dto.LoginRequest;
import com.eric.secret_vault.dto.RegisterRequest;
import com.eric.secret_vault.entity.User;
import com.eric.secret_vault.exception.ResourceAlreadyExistsException;
import com.eric.secret_vault.repository.UserRepository;
import com.eric.secret_vault.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("eric");
        registerRequest.setEmail("eric@teste.com");
        registerRequest.setPassword("senha123");
    }

    @Test
    void register_comDadosValidos_deveCriarUsuarioERetornarToken() {
        when(userRepository.existsByUsername("eric")).thenReturn(false);
        when(userRepository.existsByEmail("eric@teste.com")).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("senha-encriptada");
        when(jwtUtil.generateToken("eric")).thenReturn("token-fake");

        AuthResponse response = authService.register(registerRequest);

        assertEquals("token-fake", response.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_comUsernameJaExistente_deveLancarExcecao() {
        when(userRepository.existsByUsername("eric")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> authService.register(registerRequest));
    }

    @Test
    void register_comEmailJaExistente_deveLancarExcecao() {
        when(userRepository.existsByUsername("eric")).thenReturn(false);
        when(userRepository.existsByEmail("eric@teste.com")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> authService.register(registerRequest));
    }

    @Test
    void login_comCredenciaisValidas_deveRetornarToken() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("eric");
        loginRequest.setPassword("senha123");
        when(jwtUtil.generateToken("eric")).thenReturn("token-fake");

        AuthResponse response = authService.login(loginRequest);

        assertEquals("token-fake", response.getToken());
    }

    @Test
    void login_comCredenciaisInvalidas_devePropagarExcecao() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("eric");
        loginRequest.setPassword("senha-errada");
        doThrow(new BadCredentialsException("Credenciais invalidas"))
                .when(authenticationManager).authenticate(any());

        assertThrows(BadCredentialsException.class,
                () -> authService.login(loginRequest));
    }
}
