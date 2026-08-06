package com.eric.secret_vault.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EncryptionServiceTest {

    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        encryptionService = new EncryptionService();
        // chave precisa ter 16 caracteres (AES-128)
        ReflectionTestUtils.setField(encryptionService, "encryptionKey", "1234567890123456");
    }

    @Test
    void encryptThenDecrypt_deveRetornarValorOriginal() {
        String original = "meu-segredo-super-secreto";

        String encrypted = encryptionService.encrypt(original);
        String decrypted = encryptionService.decrypt(encrypted);

        assertEquals(original, decrypted);
    }

    @Test
    void encrypt_naoDeveRetornarOValorEmTextoPuro() {
        String original = "senha123";

        String encrypted = encryptionService.encrypt(original);

        assertNotEquals(original, encrypted);
    }

    @Test
    void encrypt_mesmoValorDuasVezes_deveGerarMesmoResultado() {
        // AES em modo ECB (sem IV) é determinístico: mesma entrada = mesma saída.
        String original = "valor-repetido";

        String encrypted1 = encryptionService.encrypt(original);
        String encrypted2 = encryptionService.encrypt(original);

        assertEquals(encrypted1, encrypted2);
    }
}
