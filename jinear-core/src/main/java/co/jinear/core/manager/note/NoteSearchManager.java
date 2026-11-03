package co.jinear.core.manager.note;

import co.jinear.core.converter.note.NoteSearchRequestToVoConverter;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.request.note.NoteSearchRequest;
import co.jinear.core.model.response.note.NotePaginatedResponse;
import co.jinear.core.model.vo.note.NoteFtsSearchVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.note.NoteSearchService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteSearchManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final NoteSearchService noteSearchService;
    private final NoteSearchRequestToVoConverter noteSearchRequestToVoConverter;

    public NotePaginatedResponse searchNotes(String workspaceId, NoteSearchRequest noteSearchRequest, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        boolean workspaceAdmin = workspaceValidator.isWorkspaceAdminOrOwner(currentAccountId, workspaceId);
        log.info("Search note has begun. accountId: {}", currentAccountId);
        NoteFtsSearchVo noteFtsSearchVo = noteSearchRequestToVoConverter.map(noteSearchRequest, workspaceId, currentAccountId, workspaceAdmin, page);
        Page<NoteDto> noteDtoPage = noteSearchService.search(noteFtsSearchVo);
        return mapResponse(noteDtoPage);
    }

    private NotePaginatedResponse mapResponse(Page<NoteDto> noteDtoPage) {
        PageDto<NoteDto> data = new PageDto<>(noteDtoPage);
        NotePaginatedResponse response = new NotePaginatedResponse();
        response.setNoteDtoPageDto(data);
        return response;
    }
}
