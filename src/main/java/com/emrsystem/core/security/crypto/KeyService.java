package com.emrsystem.core.security.crypto;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KeyService {

    private final byte[] encKey;
    private final byte[] hashKey;
    private final String keyVersion;

    public KeyService(
            @Value("${security.enc.key}") String encKeyBase64,
            @Value("${security.hash.key}") String hashKeyBase64,
            @Value("${security.key.version:v1}") String keyVersion
    ) {
        this.encKey = Base64.getDecoder().decode(encKeyBase64);
        this.hashKey = Base64.getDecoder().decode(hashKeyBase64);
        this.keyVersion = keyVersion;
    }

    public byte[] getEncryptionKey() { return encKey; }
    public byte[] getHashKey() { return hashKey; }
    public String getKeyVersion() { return keyVersion; }
}


