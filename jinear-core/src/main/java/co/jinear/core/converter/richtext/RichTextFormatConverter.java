package co.jinear.core.converter.richtext;

import co.jinear.core.model.enumtype.richtext.RichTextFormat;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter
public class RichTextFormatConverter implements AttributeConverter<RichTextFormat, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RichTextFormat attribute) {
        return Objects.isNull(attribute) ? RichTextFormat.LEGACY.getValue() : attribute.getValue();
    }

    @Override
    public RichTextFormat convertToEntityAttribute(Integer dbData) {
        return Objects.isNull(dbData) ? RichTextFormat.LEGACY : RichTextFormat.fromValue(dbData);
    }
}
