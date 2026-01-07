package co.jinear.core.converter.material;

import co.jinear.core.model.enumtype.material.MaterialAccessType;
import co.jinear.core.model.enumtype.material.MaterialType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Converter
@Component
public class MaterialAccessTypeConverter implements AttributeConverter<MaterialAccessType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MaterialAccessType type) {
        return Optional.ofNullable(type).map(MaterialAccessType::getValue).orElse(null);
    }

    @Override
    public MaterialAccessType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(MaterialAccessType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}