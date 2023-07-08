package co.jinear.core.converter.workspace;

import co.jinear.core.model.enumtype.workspace.WorkspaceTier;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class WorkspaceTierTypeConverter implements AttributeConverter<WorkspaceTier, Integer> {

    @Override
    public Integer convertToDatabaseColumn(WorkspaceTier type) {
        return Optional.ofNullable(type).map(WorkspaceTier::getValue).orElse(null);
    }

    @Override
    public WorkspaceTier convertToEntityAttribute(Integer integer) {
        return Arrays.stream(WorkspaceTier.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
