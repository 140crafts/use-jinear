package co.jinear.core.converter.integration;

import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class IntegrationProviderConverter implements AttributeConverter<IntegrationProvider, Integer> {

    @Override
    public Integer convertToDatabaseColumn(IntegrationProvider type) {
        return Optional.ofNullable(type).map(IntegrationProvider::getValue).orElse(null);
    }

    @Override
    public IntegrationProvider convertToEntityAttribute(Integer integer) {
        return Arrays.stream(IntegrationProvider.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
