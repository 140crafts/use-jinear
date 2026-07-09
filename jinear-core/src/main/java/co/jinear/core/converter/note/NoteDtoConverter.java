package co.jinear.core.converter.note;

import co.jinear.core.converter.account.PlainAccountProfileDtoConverter;
import co.jinear.core.converter.notetag.NoteTagDtoConverter;
import co.jinear.core.converter.richtext.RichTextConverter;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.note.NotePathDto;
import co.jinear.core.model.dto.note.NotePathItemDto;
import co.jinear.core.model.dto.note.PathAwareNoteDto;
import co.jinear.core.model.entity.note.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {PlainAccountProfileDtoConverter.class, NoteTagDtoConverter.class, RichTextConverter.class})
public interface NoteDtoConverter {

    NoteDto convert(Note note);

    @Mapping(target = "noteId", source = "noteDto.noteId")
    @Mapping(target = "notePath", source = "notePath")
    PathAwareNoteDto convert(NoteDto noteDto, NotePathDto notePath);

    default PathAwareNoteDto convertChildWithPath(NoteDto child, NotePathDto parentPath) {
        List<NotePathItemDto> path = new ArrayList<>();
        Optional.ofNullable(parentPath).map(NotePathDto::getPath).ifPresent(path::addAll);
        path.add(NotePathItemDto.builder()
                .noteId(child.getNoteId())
                .parentNoteId(child.getParentNoteId())
                .title(child.getTitle())
                .build());
        NotePathDto childPath = NotePathDto.builder()
                .noteId(child.getNoteId())
                .path(path)
                .fullPath(path.stream()
                        .map(NotePathItemDto::getTitle)
                        .collect(Collectors.joining(" / ")))
                .build();
        return convert(child, childPath);
    }
}
