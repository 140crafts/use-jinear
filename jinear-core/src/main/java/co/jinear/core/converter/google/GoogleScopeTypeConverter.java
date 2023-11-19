package co.jinear.core.converter.google;

import co.jinear.core.model.enumtype.google.GoogleScopeType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class GoogleScopeTypeConverter implements AttributeConverter<GoogleScopeType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(GoogleScopeType type) {
        return Optional.ofNullable(type).map(GoogleScopeType::getValue).orElse(null);
    }

    @Override
    public GoogleScopeType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(GoogleScopeType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
