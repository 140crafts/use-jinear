package co.jinear.core.converter.media;

import co.jinear.core.model.enumtype.media.MediaFileUploadStatusType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class MediaFileUploadStatusTypeConverter implements AttributeConverter<MediaFileUploadStatusType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MediaFileUploadStatusType type) {
        return Optional.ofNullable(type).map(MediaFileUploadStatusType::getValue).orElse(null);
    }

    @Override
    public MediaFileUploadStatusType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(MediaFileUploadStatusType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
