package co.jinear.core.converter.project;

import co.jinear.core.model.enumtype.project.MilestoneStateType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class MilestoneStateTypeConverter implements AttributeConverter<MilestoneStateType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MilestoneStateType type) {
        return Optional.ofNullable(type).map(MilestoneStateType::getValue).orElse(null);
    }

    @Override
    public MilestoneStateType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(MilestoneStateType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
