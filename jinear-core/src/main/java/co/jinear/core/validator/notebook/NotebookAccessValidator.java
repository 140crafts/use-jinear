package co.jinear.core.validator.notebook;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.enumtype.notebook.NotebookVisibilityType;
import co.jinear.core.service.note.notebook.NotebookMemberRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotebookAccessValidator {

    private final NotebookMemberRetrieveService notebookMemberRetrieveService;
    private final WorkspaceValidator workspaceValidator;

    public void validateHasAccess(String accountId, NotebookDto notebookDto) {
        if (Boolean.FALSE.equals(hasAccess(accountId, notebookDto))) {
            log.info("Account does not have notebook access. accountId: {}, notebookId: {}", accountId, notebookDto.getNotebookId());
            throw new NoAccessException();
        }
    }

    public void validateHasManageAccess(String accountId, NotebookDto notebookDto) {
        if (Boolean.FALSE.equals(hasManageAccess(accountId, notebookDto))) {
            log.info("Account does not have notebook manage access. accountId: {}, notebookId: {}", accountId, notebookDto.getNotebookId());
            throw new NoAccessException();
        }
    }

    private boolean hasAccess(String accountId, NotebookDto notebookDto) {
        boolean isOwner = notebookDto.getOwnerId().equals(accountId);
        if (isOwner) {
            return true;
        }
        if (NotebookVisibilityType.SHARED.equals(notebookDto.getVisibility())
                && notebookMemberRetrieveService.isNotebookMember(accountId, notebookDto.getNotebookId())) {
            return true;
        }
        return workspaceValidator.isWorkspaceAdminOrOwner(accountId, notebookDto.getWorkspaceId());
    }

    private boolean hasManageAccess(String accountId, NotebookDto notebookDto) {
        boolean isOwner = notebookDto.getOwnerId().equals(accountId);
        return isOwner || workspaceValidator.isWorkspaceAdminOrOwner(accountId, notebookDto.getWorkspaceId());
    }
}
