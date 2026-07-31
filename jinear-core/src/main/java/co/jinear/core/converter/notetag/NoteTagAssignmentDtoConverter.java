package co.jinear.core.converter.notetag;

import co.jinear.core.model.dto.notetag.NoteTagAssignmentDto;
import co.jinear.core.model.entity.note.NoteTagAssignment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {NoteTagDtoConverter.class})
public interface NoteTagAssignmentDtoConverter {

    NoteTagAssignmentDto convert(NoteTagAssignment noteTagAssignment);
}
