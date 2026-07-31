package co.jinear.core.converter.notetag;

import co.jinear.core.model.dto.notetag.NoteTagDto;
import co.jinear.core.model.entity.note.NoteTag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NoteTagDtoConverter {

    NoteTagDto convert(NoteTag noteTag);
}
