package co.jinear.core.converter.note;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.converter.notebook.NotebookDtoConverter;
import co.jinear.core.converter.notetag.NoteTagAssignmentDtoConverter;
import co.jinear.core.converter.notetag.NoteTagDtoConverter;
import co.jinear.core.converter.richtext.RichTextConverter;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.entity.note.Note;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        PlainAccountProfileDtoConverter.class,
        NoteTagDtoConverter.class,
        RichTextConverter.class,
        NotebookDtoConverter.class,
        NoteTagAssignmentDtoConverter.class
})
public interface NoteDtoConverter {

    NoteDto convert(Note note);
}
