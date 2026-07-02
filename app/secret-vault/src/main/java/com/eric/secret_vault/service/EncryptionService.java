package com.eric.secret_vault.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptionService {

    @Value("${encryption.key}")
    private String encryptionKey;

    private static final String ALGORITHM = "AES";

    public String encrypt(String value) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar valor", e);
        }
    }

    public String decrypt(String encryptedValue) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = Base64.getDecoder().decode(encryptedValue);
            return new String(cipher.doFinal(decoded));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar valor", e);
        }
    }
}
