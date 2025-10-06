package com.emrsystem.core.security.crypto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Converter
@Component
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    private static KeyService keyService;

    @Autowired
    public void setKeyService(KeyService keyService) {
        EncryptedStringConverter.keyService = keyService;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        return AesGcmEncryptor.encrypt(keyService.getEncryptionKey(), attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return AesGcmEncryptor.decrypt(keyService.getEncryptionKey(), dbData);
    }
}


