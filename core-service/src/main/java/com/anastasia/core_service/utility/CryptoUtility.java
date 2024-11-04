package com.anastasia.core_service.utility;

public interface CryptoUtility {

    String encrypt(String value);

    String decrypt(String encryptedValue);
}
