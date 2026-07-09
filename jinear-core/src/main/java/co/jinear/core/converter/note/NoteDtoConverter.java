package co.jinear.core.converter.note;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.converter.notetag.NoteTagDtoConverter;
import co.jinear.core.converter.richtext.RichTextConverter;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.note.NotePathDto;
import co.jinear.core.model.dto.note.PathAwareNoteDto;
import co.jinear.core.model.entity.note.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PlainAccountProfileDtoConverter.class, NoteTagDtoConverter.class, RichTextConverter.class})
public interface NoteDtoConverter {

    NoteDto convert(Note note);

    @Mapping(target = "noteId", source = "noteDto.noteId")
    @Mapping(target = "notePath", source = "notePath")
    PathAwareNoteDto convert(NoteDto noteDto, NotePathDto notePath);
}
