package com.project.core.utility.impl;

import com.project.core.utility.CryptoUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
@Component
public class AESCryptoUtility implements CryptoUtility {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private final String SECRET_KEY;
    private final String INIT_VECTOR;


    @Autowired
    public AESCryptoUtility(@Value("${project.encryption.secret-key}") String secretKey,
                            @Value("${project.encryption.iv-value}") String initVector) {
        SECRET_KEY = secretKey;
        INIT_VECTOR = initVector;
    }


    @Override
    public String encrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] ivBytes = Base64.getDecoder().decode(INIT_VECTOR);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }

    }

    @Override
    public String decrypt(String encryptedValue) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] ivBytes = Base64.getDecoder().decode(INIT_VECTOR);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(decrypted, StandardCharsets.UTF_8);

        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }
}
