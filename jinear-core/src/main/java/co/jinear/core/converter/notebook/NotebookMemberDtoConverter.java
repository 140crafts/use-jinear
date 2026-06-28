package co.jinear.core.converter.notebook;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.model.dto.notebook.NotebookMemberDto;
import co.jinear.core.model.entity.note.NotebookMember;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PlainAccountProfileDtoConverter.class})
public interface NotebookMemberDtoConverter {

    NotebookMemberDto convert(NotebookMember notebookMember);
}
