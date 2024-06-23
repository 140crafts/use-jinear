package co.jinear.core.converter.robot;

import co.jinear.core.model.enumtype.robot.RobotType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class RobotTypeConverter implements AttributeConverter<RobotType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RobotType type) {
        return Optional.ofNullable(type).map(RobotType::getValue).orElse(null);
    }

    @Override
    public RobotType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(RobotType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}

