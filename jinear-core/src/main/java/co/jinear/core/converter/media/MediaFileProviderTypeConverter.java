package co.jinear.core.converter.media;

import co.jinear.core.model.enumtype.media.MediaFileProviderType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class MediaFileProviderTypeConverter implements AttributeConverter<MediaFileProviderType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MediaFileProviderType type) {
        return Optional.ofNullable(type).map(MediaFileProviderType::getValue).orElse(null);
    }

    @Override
    public MediaFileProviderType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(MediaFileProviderType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
