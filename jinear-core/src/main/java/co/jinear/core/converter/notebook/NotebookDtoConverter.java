package co.jinear.core.converter.notebook;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.entity.note.Notebook;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PlainAccountProfileDtoConverter.class})
public interface NotebookDtoConverter {

    NotebookDto convert(Notebook notebook);
}
