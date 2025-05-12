package co.jinear.core.converter.media;

import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class MediaVisibilityTypeConverter implements AttributeConverter<MediaVisibilityType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MediaVisibilityType type) {
        return Optional.ofNullable(type).map(MediaVisibilityType::getValue).orElse(null);
    }

    @Override
    public MediaVisibilityType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(MediaVisibilityType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
