package co.jinear.core.converter.material;

import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class MaterialTypeConverter implements AttributeConverter<MaterialType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MaterialType type) {
        return Optional.ofNullable(type).map(MaterialType::getValue).orElse(null);
    }

    @Override
    public MaterialType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(MaterialType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}