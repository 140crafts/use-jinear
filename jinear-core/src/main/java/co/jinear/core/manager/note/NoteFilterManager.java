package co.jinear.core.manager.note;

import co.jinear.core.converter.note.NoteFilterRequestToVoConverter;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.request.note.NoteFilterRequest;
import co.jinear.core.model.response.note.NotePaginatedResponse;
import co.jinear.core.model.vo.note.NoteFilterVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.note.NoteFilterService;
import co.jinear.core.service.note.notebook.NotebookRetrieveService;
import co.jinear.core.validator.notebook.NotebookAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteFilterManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final NotebookRetrieveService notebookRetrieveService;
    private final NotebookAccessValidator notebookAccessValidator;
    private final NoteFilterRequestToVoConverter noteFilterRequestToVoConverter;
    private final NoteFilterService noteFilterService;

    public NotePaginatedResponse filter(NoteFilterRequest noteFilterRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, noteFilterRequest.getWorkspaceId());
        validateNotebookAccess(currentAccountId, noteFilterRequest.getNotebookId());
        NoteFilterVo noteFilterVo = noteFilterRequestToVoConverter.map(noteFilterRequest, currentAccountId);
        Page<NoteDto> noteDtos = noteFilterService.filter(noteFilterVo);

        return mapResponse(noteDtos);
    }

    private void validateNotebookAccess(String currentAccountId, String notebookId) {
        if (Objects.nonNull(notebookId)) {
            NotebookDto notebookDto = notebookRetrieveService.retrieve(notebookId);
            notebookAccessValidator.validateHasAccess(currentAccountId, notebookDto);
        }
    }

    private NotePaginatedResponse mapResponse(Page<NoteDto> noteDtos) {
        NotePaginatedResponse notePaginatedResponse = new NotePaginatedResponse();
        notePaginatedResponse.setNoteDtoPageDto(new PageDto<>(noteDtos));
        return notePaginatedResponse;
    }
}
