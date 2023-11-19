package co.jinear.core.converter;

import co.jinear.core.system.util.CryptoHelper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Optional;

@Converter
public class EncryptedColumnConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String plain) {
        return Optional.ofNullable(plain)
                .map(CryptoHelper::encrypt)
                .orElse(null);
    }

    @Override
    public String convertToEntityAttribute(String encrypted) {
        return Optional.ofNullable(encrypted)
                .map(CryptoHelper::decrypt)
                .orElse(null);
    }
}
