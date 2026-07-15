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
    NoteFilterVo map(NoteFilterRequest noteFilterRequest, String currentAccountId);

    @AfterMapping
    default void afterMap(@MappingTarget NoteFilterVo noteFilterVo, String currentAccountId) {
        boolean isListingDrafts = Objects.isNull(noteFilterVo.getNotebookId());
        if (isListingDrafts){
            noteFilterVo.setOwnerId(currentAccountId);
        }
    }
}
