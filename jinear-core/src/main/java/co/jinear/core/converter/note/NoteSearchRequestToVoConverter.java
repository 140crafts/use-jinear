package co.jinear.core.converter.note;

import co.jinear.core.model.request.note.NoteSearchRequest;
import co.jinear.core.model.vo.note.NoteFtsSearchVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NoteSearchRequestToVoConverter {

    @Mapping(target = "query", source = "noteSearchRequest.query")
    @Mapping(target = "notebookId", source = "noteSearchRequest.notebookId")
    @Mapping(target = "parentNoteId", source = "noteSearchRequest.parentNoteId")
    @Mapping(target = "workspaceId", source = "workspaceId")
    @Mapping(target = "accountId", source = "accountId")
    @Mapping(target = "workspaceAdmin", source = "workspaceAdmin")
    @Mapping(target = "page", source = "page")
    NoteFtsSearchVo map(NoteSearchRequest noteSearchRequest, String workspaceId, String accountId, boolean workspaceAdmin, int page);
}
