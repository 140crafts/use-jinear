package co.jinear.core.converter.media;

import co.jinear.core.model.enumtype.media.MediaFileOwnershipStatusType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class MediaFileOwnershipStatusTypeConverter implements AttributeConverter<MediaFileOwnershipStatusType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MediaFileOwnershipStatusType type) {
        return Optional.ofNullable(type).map(MediaFileOwnershipStatusType::getValue).orElse(null);
    }

    @Override
    public MediaFileOwnershipStatusType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(MediaFileOwnershipStatusType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
