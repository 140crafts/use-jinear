package co.jinear.core.converter.management;

import co.jinear.core.model.enumtype.management.InstanceFlagType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Converter
@Component
public class InstanceFlagTypeConverter implements AttributeConverter<InstanceFlagType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(InstanceFlagType type) {
        return Optional.ofNullable(type).map(InstanceFlagType::getValue).orElse(null);
    }

    @Override
    public InstanceFlagType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(InstanceFlagType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}