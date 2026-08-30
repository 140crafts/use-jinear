package co.jinear.core.converter.mcp;

import co.jinear.core.model.enumtype.mcp.McpToolCallStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class McpToolCallStatusConverter implements AttributeConverter<McpToolCallStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(McpToolCallStatus status) {
        return Optional.ofNullable(status).map(McpToolCallStatus::getValue).orElse(null);
    }

    @Override
    public McpToolCallStatus convertToEntityAttribute(Integer integer) {
        return Arrays.stream(McpToolCallStatus.values())
                .filter(status -> status.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
