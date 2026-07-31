package co.jinear.core.converter.notebook;

import co.jinear.core.model.enumtype.notebook.NotebookVisibilityType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter
public class NotebookVisibilityTypeConverter implements AttributeConverter<NotebookVisibilityType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(NotebookVisibilityType attribute) {
        return Objects.isNull(attribute) ? NotebookVisibilityType.PRIVATE.getValue() : attribute.getValue();
    }

    @Override
    public NotebookVisibilityType convertToEntityAttribute(Integer dbData) {
        return Objects.isNull(dbData) ? NotebookVisibilityType.PRIVATE : NotebookVisibilityType.fromValue(dbData);
    }
}
