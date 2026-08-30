package co.jinear.core.converter.mcp;

import co.jinear.core.model.enumtype.mcp.McpClientRegistrationType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class McpClientRegistrationTypeConverter implements AttributeConverter<McpClientRegistrationType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(McpClientRegistrationType type) {
        return Optional.ofNullable(type).map(McpClientRegistrationType::getValue).orElse(null);
    }

    @Override
    public McpClientRegistrationType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(McpClientRegistrationType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
