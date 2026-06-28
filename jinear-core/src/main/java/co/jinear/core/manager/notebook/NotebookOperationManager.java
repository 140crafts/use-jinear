package co.jinear.core.manager.notebook;

import co.jinear.core.converter.notebook.NotebookInitializeRequestToVoConverter;
import co.jinear.core.converter.notebook.NotebookUpdateRequestToVoConverter;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.request.notebook.NotebookInitializeRequest;
import co.jinear.core.model.request.notebook.NotebookUpdateRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.notebook.NotebookInitializeResponse;
import co.jinear.core.model.vo.notebook.NotebookInitializeVo;
import co.jinear.core.model.vo.notebook.NotebookUpdateVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.note.notebook.NotebookOperationService;
import co.jinear.core.service.note.notebook.NotebookRetrieveService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.validator.notebook.NotebookAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotebookOperationManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final NotebookAccessValidator notebookAccessValidator;
    private final NotebookOperationService notebookOperationService;
    private final NotebookRetrieveService notebookRetrieveService;
    private final NotebookInitializeRequestToVoConverter notebookInitializeRequestToVoConverter;
    private final NotebookUpdateRequestToVoConverter notebookUpdateRequestToVoConverter;
    private final PassiveService passiveService;

    public NotebookInitializeResponse initialize(NotebookInitializeRequest request, String workspaceId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Initialize notebook has started. currentAccountId: {}", currentAccountId);
        NotebookInitializeVo notebookInitializeVo = notebookInitializeRequestToVoConverter.convert(request, workspaceId, currentAccountId);
        String notebookId = notebookOperationService.initialize(notebookInitializeVo);
        NotebookInitializeResponse response = new NotebookInitializeResponse();
        response.setNotebookId(notebookId);
        return response;
    }

    public BaseResponse update(NotebookUpdateRequest request) {
        String currentAccountId = sessionInfoService.currentAccountId();
        NotebookDto notebookDto = notebookRetrieveService.retrieve(request.getNotebookId());
        workspaceValidator.validateHasAccess(currentAccountId, notebookDto.getWorkspaceId());
        notebookAccessValidator.validateHasManageAccess(currentAccountId, notebookDto);
        log.info("Update notebook has started. currentAccountId: {}", currentAccountId);
        NotebookUpdateVo notebookUpdateVo = notebookUpdateRequestToVoConverter.convert(request);
        notebookOperationService.update(notebookUpdateVo);
        return new BaseResponse();
    }

    public BaseResponse delete(String notebookId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        NotebookDto notebookDto = notebookRetrieveService.retrieve(notebookId);
        workspaceValidator.validateHasAccess(currentAccountId, notebookDto.getWorkspaceId());
        notebookAccessValidator.validateHasManageAccess(currentAccountId, notebookDto);
        log.info("Delete notebook has started. currentAccountId: {}", currentAccountId);
        String passiveId = notebookOperationService.delete(notebookId);
        passiveService.assignOwnership(passiveId, currentAccountId);
        return new BaseResponse();
    }
}
