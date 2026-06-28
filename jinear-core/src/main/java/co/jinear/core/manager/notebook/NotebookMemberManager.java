package co.jinear.core.manager.notebook;

import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.dto.notebook.NotebookMemberDto;
import co.jinear.core.model.enumtype.notebook.NotebookVisibilityType;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.notebook.NotebookMemberPaginatedResponse;
import co.jinear.core.model.vo.notebook.AddNotebookMemberVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.note.notebook.NotebookMemberOperationService;
import co.jinear.core.service.note.notebook.NotebookMemberRetrieveService;
import co.jinear.core.service.note.notebook.NotebookRetrieveService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.validator.notebook.NotebookAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotebookMemberManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final NotebookAccessValidator notebookAccessValidator;
    private final NotebookRetrieveService notebookRetrieveService;
    private final NotebookMemberRetrieveService notebookMemberRetrieveService;
    private final NotebookMemberOperationService notebookMemberOperationService;
    private final PassiveService passiveService;

    public NotebookMemberPaginatedResponse listMembers(String notebookId, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        NotebookDto notebookDto = notebookRetrieveService.retrieve(notebookId);
        workspaceValidator.validateHasAccess(currentAccountId, notebookDto.getWorkspaceId());
        notebookAccessValidator.validateHasAccess(currentAccountId, notebookDto);
        Page<NotebookMemberDto> members = notebookMemberRetrieveService.retrieveNotebookMembers(notebookId, page);
        NotebookMemberPaginatedResponse response = new NotebookMemberPaginatedResponse();
        response.setNotebookMemberDtoPageDto(new PageDto<>(members));
        return response;
    }

    public BaseResponse addMember(String notebookId, String accountId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        NotebookDto notebookDto = notebookRetrieveService.retrieve(notebookId);
        workspaceValidator.validateHasAccess(currentAccountId, notebookDto.getWorkspaceId());
        notebookAccessValidator.validateHasManageAccess(currentAccountId, notebookDto);
        validateNotebookIsShared(notebookDto);
        workspaceValidator.validateAllHasAccess(List.of(accountId), notebookDto.getWorkspaceId());
        log.info("Add notebook member has started. currentAccountId: {}, notebookId: {}, accountId: {}", currentAccountId, notebookId, accountId);
        AddNotebookMemberVo addNotebookMemberVo = new AddNotebookMemberVo();
        addNotebookMemberVo.setNotebookId(notebookId);
        addNotebookMemberVo.setAccountId(accountId);
        addNotebookMemberVo.setWorkspaceId(notebookDto.getWorkspaceId());
        notebookMemberOperationService.addNotebookMember(addNotebookMemberVo);
        return new BaseResponse();
    }

    public BaseResponse removeMember(String notebookId, String accountId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        NotebookDto notebookDto = notebookRetrieveService.retrieve(notebookId);
        workspaceValidator.validateHasAccess(currentAccountId, notebookDto.getWorkspaceId());
        notebookAccessValidator.validateHasManageAccess(currentAccountId, notebookDto);
        log.info("Remove notebook member has started. currentAccountId: {}, notebookId: {}, accountId: {}", currentAccountId, notebookId, accountId);
        String passiveId = passiveService.createUserActionPassive(currentAccountId);
        notebookMemberOperationService.removeNotebookMember(notebookId, accountId, passiveId);
        return new BaseResponse();
    }

    private void validateNotebookIsShared(NotebookDto notebookDto) {
        if (!NotebookVisibilityType.SHARED.equals(notebookDto.getVisibility())) {
            log.info("Cannot add members to a non-shared notebook. notebookId: {}", notebookDto.getNotebookId());
            throw new NotValidException();
        }
    }
}
