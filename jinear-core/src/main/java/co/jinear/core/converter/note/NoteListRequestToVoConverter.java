package co.jinear.core.converter.note;

import co.jinear.core.model.request.note.NoteListRequest;
import co.jinear.core.model.vo.note.NoteListVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NoteListRequestToVoConverter {

    @Mapping(target = "notebookId", source = "noteListRequest.notebookId")
    @Mapping(target = "parentNoteId", source = "noteListRequest.parentNoteId")
    @Mapping(target = "workspaceId", source = "workspaceId")
    @Mapping(target = "accountId", source = "accountId")
    @Mapping(target = "workspaceAdmin", source = "workspaceAdmin")
    @Mapping(target = "page", source = "page")
    NoteListVo map(NoteListRequest noteListRequest, String workspaceId, String accountId, boolean workspaceAdmin, int page);
}
