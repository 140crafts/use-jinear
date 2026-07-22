package co.jinear.core.converter.note;

import co.jinear.core.model.request.note.NoteFilterRequest;
import co.jinear.core.model.vo.note.NoteFilterVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface NoteFilterRequestToVoConverter {

    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "notebookIdIsNull", ignore = true)
    @Mapping(target = "parentNoteIdIsNull", ignore = true)
    NoteFilterVo map(NoteFilterRequest noteFilterRequest, String currentAccountId);

    @AfterMapping
    default void afterMap(@MappingTarget NoteFilterVo noteFilterVo, String currentAccountId) {
        boolean isListingDrafts = Objects.isNull(noteFilterVo.getNotebookId()) && Objects.isNull(noteFilterVo.getNoteId());
        if (isListingDrafts) {
            noteFilterVo.setOwnerId(currentAccountId);
            noteFilterVo.setNotebookIdIsNull(true);
        }
        boolean retrievingExactNote = Objects.nonNull(noteFilterVo.getNoteId());
        boolean parentNoteIdProvided = Objects.nonNull(noteFilterVo.getParentNoteId());
        noteFilterVo.setParentNoteIdIsNull(!retrievingExactNote && !parentNoteIdProvided);
    }
}
