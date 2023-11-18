package co.jinear.core.converter.integration;

import co.jinear.core.model.enumtype.integration.IntegrationScopeType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class IntegrationScopeTypeConverter implements AttributeConverter<IntegrationScopeType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(IntegrationScopeType type) {
        return Optional.ofNullable(type).map(IntegrationScopeType::getValue).orElse(null);
    }

    @Override
    public IntegrationScopeType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(IntegrationScopeType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
