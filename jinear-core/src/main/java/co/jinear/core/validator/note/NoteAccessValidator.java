package co.jinear.core.validator.note;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.service.note.notebook.NotebookRetrieveService;
import co.jinear.core.validator.notebook.NotebookAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class NoteAccessValidator {

    private final WorkspaceValidator workspaceValidator;
    private final NotebookRetrieveService notebookRetrieveService;
    private final NotebookAccessValidator notebookAccessValidator;

    public void validateCanCreate(String accountId, String workspaceId, String notebookId) {
        workspaceValidator.validateHasAccess(accountId, workspaceId);
        if (Objects.nonNull(notebookId)) {
            validateNotebookInWorkspace(accountId, workspaceId, notebookId);
        }
    }

    public void validateCanEdit(String accountId, String workspaceId, NoteDto note) {
        workspaceValidator.validateHasAccess(accountId, workspaceId);
        if (!workspaceId.equalsIgnoreCase(note.getWorkspaceId())) {
            throw new NoAccessException();
        }
        if (!accountId.equalsIgnoreCase(note.getOwnerId())) {
            validateNonOwnerAccess(accountId, workspaceId, note);
        }
    }

    private void validateNonOwnerAccess(String accountId, String workspaceId, NoteDto note) {
        if (Objects.isNull(note.getNotebookId())) {
            throw new NoAccessException();
        }
        validateNotebookInWorkspace(accountId, workspaceId, note.getNotebookId());
    }

    private void validateNotebookInWorkspace(String accountId, String workspaceId, String notebookId) {
        NotebookDto notebook = notebookRetrieveService.retrieve(notebookId);
        if (!workspaceId.equalsIgnoreCase(notebook.getWorkspaceId())) {
            throw new NoAccessException();
        }
        notebookAccessValidator.validateHasAccess(accountId, notebook);
    }
}
