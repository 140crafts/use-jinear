package co.jinear.core.converter.media;

import co.jinear.core.model.enumtype.media.MediaFileUploadMethodType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Optional;

@Converter
public class MediaFileUploadMethodTypeConverter implements AttributeConverter<MediaFileUploadMethodType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MediaFileUploadMethodType type) {
        return Optional.ofNullable(type).map(MediaFileUploadMethodType::getValue).orElse(null);
    }

    @Override
    public MediaFileUploadMethodType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(MediaFileUploadMethodType.values())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElse(null);
    }
}
