package com.eric.secret_vault.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "chave-de-teste-com-tamanho-suficiente-para-hs256");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L); // 24h
    }

    @Test
    void generateToken_deveGerarTokenNaoNulo() {
        String token = jwtUtil.generateToken("eric");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void extractUsername_deveRetornarUsernameCorreto() {
        String token = jwtUtil.generateToken("eric");

        String username = jwtUtil.extractUsername(token);

        assertEquals("eric", username);
    }

    @Test
    void isTokenValid_comUsernameCorretoENaoExpirado_deveRetornarTrue() {
        String token = jwtUtil.generateToken("eric");

        assertTrue(jwtUtil.isTokenValid(token, "eric"));
    }

    @Test
    void isTokenValid_comUsernameDiferente_deveRetornarFalse() {
        String token = jwtUtil.generateToken("eric");

        assertFalse(jwtUtil.isTokenValid(token, "outro-usuario"));
    }

    @Test
    void isTokenValid_comTokenExpirado_deveRetornarFalse() {
        // expiração negativa força o token a já nascer expirado
        ReflectionTestUtils.setField(jwtUtil, "expiration", -1000L);
        String tokenExpirado = jwtUtil.generateToken("eric");

        assertFalse(jwtUtil.isTokenValid(tokenExpirado, "eric"));
    }
}
