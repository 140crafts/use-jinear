package co.jinear.core.note;

import co.jinear.core.converter.note.NoteInitializeRequestToVoConverter;
import co.jinear.core.converter.note.NoteUpdateRequestToVoConverter;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.manager.note.NoteOperationManager;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.request.note.NoteMoveRequest;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.note.IdempotentNoteCreateService;
import co.jinear.core.service.note.NoteOperationService;
import co.jinear.core.service.note.NoteRetrieveService;
import co.jinear.core.service.note.notebook.NotebookRetrieveService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.validator.note.NoteAccessValidator;
import co.jinear.core.validator.notebook.NotebookAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class NoteOperationManagerAccessTest {

    private static final String WORKSPACE_ID = "workspace-1";
    private static final String OWNER_ID = "owner";
    private static final String OTHER_MEMBER_ID = "other-member";
    private static final String DRAFT_ID = "draft-1";
    private static final String NOTEBOOK_NOTE_ID = "note-1";
    private static final String TARGET_NOTEBOOK_ID = "notebook-2";

    private final SessionInfoService sessionInfoService = Mockito.mock(SessionInfoService.class);
    private final NotebookRetrieveService notebookRetrieveService = Mockito.mock(NotebookRetrieveService.class);
    private final NotebookAccessValidator notebookAccessValidator = Mockito.mock(NotebookAccessValidator.class);
    private final WorkspaceValidator workspaceValidator = Mockito.mock(WorkspaceValidator.class);
    private final NoteRetrieveService noteRetrieveService = Mockito.mock(NoteRetrieveService.class);
    private final NoteOperationService noteOperationService = Mockito.mock(NoteOperationService.class);
    private final NoteOperationManager noteOperationManager = new NoteOperationManager(
            sessionInfoService,
            workspaceValidator,
            notebookAccessValidator,
            new NoteAccessValidator(workspaceValidator, notebookRetrieveService, notebookAccessValidator),
            notebookRetrieveService,
            noteRetrieveService,
            noteOperationService,
            Mockito.mock(IdempotentNoteCreateService.class),
            Mockito.mock(NoteInitializeRequestToVoConverter.class),
            Mockito.mock(NoteUpdateRequestToVoConverter.class),
            Mockito.mock(PassiveService.class));

    @BeforeEach
    void setUp() {
        Mockito.when(noteRetrieveService.retrieve(DRAFT_ID)).thenReturn(note(DRAFT_ID, null, WORKSPACE_ID));
        Mockito.when(noteRetrieveService.retrieve(NOTEBOOK_NOTE_ID)).thenReturn(note(NOTEBOOK_NOTE_ID, "notebook-1", WORKSPACE_ID));
        Mockito.when(notebookRetrieveService.retrieve(anyString())).thenAnswer(invocation -> notebook(invocation.getArgument(0)));
    }

    @Test
    void anotherMemberCanNotMoveADraftIntoANotebook() {
        signInAs(OTHER_MEMBER_ID);

        assertThatThrownBy(() -> noteOperationManager.changeNotebook(WORKSPACE_ID, DRAFT_ID, TARGET_NOTEBOOK_ID))
                .isInstanceOf(NoAccessException.class);
        Mockito.verify(noteOperationService, Mockito.never()).changeNotebook(anyString(), anyString());
    }

    @Test
    void theOwnerCanMoveADraftIntoANotebook() {
        signInAs(OWNER_ID);

        noteOperationManager.changeNotebook(WORKSPACE_ID, DRAFT_ID, TARGET_NOTEBOOK_ID);

        Mockito.verify(noteOperationService).changeNotebook(DRAFT_ID, TARGET_NOTEBOOK_ID);
    }

    @Test
    void anotherMemberCanNotDeleteADraft() {
        signInAs(OTHER_MEMBER_ID);

        assertThatThrownBy(() -> noteOperationManager.delete(WORKSPACE_ID, DRAFT_ID))
                .isInstanceOf(NoAccessException.class);
        Mockito.verify(noteOperationService, Mockito.never()).delete(anyString(), any());
    }

    @Test
    void anotherMemberCanNotMoveADraft() {
        signInAs(OTHER_MEMBER_ID);
        NoteMoveRequest request = new NoteMoveRequest();
        request.setNoteId(DRAFT_ID);

        assertThatThrownBy(() -> noteOperationManager.move(WORKSPACE_ID, request))
                .isInstanceOf(NoAccessException.class);
        Mockito.verify(noteOperationService, Mockito.never()).moveTo(anyString(), any());
    }

    @Test
    void anotherMemberWithNotebookAccessCanMoveANotebookNote() {
        signInAs(OTHER_MEMBER_ID);
        NoteMoveRequest request = new NoteMoveRequest();
        request.setNoteId(NOTEBOOK_NOTE_ID);

        noteOperationManager.move(WORKSPACE_ID, request);

        Mockito.verify(noteOperationService).moveTo(NOTEBOOK_NOTE_ID, null);
    }

    @Test
    void aNoteOfAnotherWorkspaceCanNotBeChangedThroughThisWorkspace() {
        signInAs(OWNER_ID);

        assertThatThrownBy(() -> noteOperationManager.delete("workspace-2", DRAFT_ID))
                .isInstanceOf(NoAccessException.class);
        Mockito.verify(noteOperationService, Mockito.never()).delete(anyString(), any());
    }

    private void signInAs(String accountId) {
        Mockito.when(sessionInfoService.currentAccountId()).thenReturn(accountId);
    }

    private NoteDto note(String noteId, String notebookId, String workspaceId) {
        NoteDto noteDto = new NoteDto();
        noteDto.setNoteId(noteId);
        noteDto.setNotebookId(notebookId);
        noteDto.setWorkspaceId(workspaceId);
        noteDto.setOwnerId(OWNER_ID);
        return noteDto;
    }

    private NotebookDto notebook(String notebookId) {
        NotebookDto notebookDto = new NotebookDto();
        notebookDto.setNotebookId(notebookId);
        notebookDto.setWorkspaceId(WORKSPACE_ID);
        notebookDto.setOwnerId(OWNER_ID);
        return notebookDto;
    }
}
