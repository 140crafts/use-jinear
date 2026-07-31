package co.jinear.core.manager.notebook;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.response.notebook.NotebookListingResponse;
import co.jinear.core.model.response.notebook.NotebookResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.note.notebook.NotebookRetrieveService;
import co.jinear.core.validator.notebook.NotebookAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotebookListingManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final NotebookAccessValidator notebookAccessValidator;
    private final NotebookRetrieveService notebookRetrieveService;

    public NotebookResponse retrieve(String notebookId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        NotebookDto notebookDto = notebookRetrieveService.retrieve(notebookId);
        workspaceValidator.validateHasAccess(currentAccountId, notebookDto.getWorkspaceId());
        notebookAccessValidator.validateHasAccess(currentAccountId, notebookDto);
        NotebookResponse response = new NotebookResponse();
        response.setNotebookDto(notebookDto);
        return response;
    }

    public NotebookListingResponse listWorkspaceNotebooks(String workspaceId, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("List workspace notebooks has started. currentAccountId: {}, workspaceId: {}", currentAccountId, workspaceId);
        boolean workspaceAdminOrOwner = workspaceValidator.isWorkspaceAdminOrOwner(currentAccountId, workspaceId);
        Page<NotebookDto> notebooks = workspaceAdminOrOwner
                ? notebookRetrieveService.retrieveWorkspaceNotebooks(workspaceId, page)
                : notebookRetrieveService.retrieveAccessibleNotebooks(workspaceId, currentAccountId, page);
        NotebookListingResponse response = new NotebookListingResponse();
        response.setNotebookDtoPageDto(new PageDto<>(notebooks));
        return response;
    }
}
