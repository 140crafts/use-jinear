package co.jinear.core.converter.note;

import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.note.TaskLinkingNoteDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskLinkingNoteDtoConverter {

    @Mapping(target = "notebookTitle", source = "notebook.title")
    TaskLinkingNoteDto convert(NoteDto noteDto);
}
