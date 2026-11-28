package co.jinear.core.note;

import co.jinear.core.converter.note.TaskLinkingNoteDtoConverter;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.manager.note.TaskLinkingNoteManager;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.notebook.NotebookVisibilityType;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.note.NoteRetrieveService;
import co.jinear.core.service.note.notebook.NotebookMemberRetrieveService;
import co.jinear.core.service.note.notebook.NotebookRetrieveService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.validator.note.NoteAccessValidator;
import co.jinear.core.validator.notebook.NotebookAccessValidator;
import co.jinear.core.validator.task.TaskTeamVisibilityTypeAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskLinkingNoteAccessTest {

    private static final String WORKSPACE_ID = "workspace-1";
    private static final String VIEWER = "viewer";

    private final WorkspaceValidator workspaceValidator = Mockito.mock(WorkspaceValidator.class);
    private final NotebookRetrieveService notebookRetrieveService = Mockito.mock(NotebookRetrieveService.class);
    private final NotebookMemberRetrieveService notebookMemberRetrieveService = Mockito.mock(NotebookMemberRetrieveService.class);
    private final NotebookAccessValidator notebookAccessValidator = new NotebookAccessValidator(notebookMemberRetrieveService, workspaceValidator);
    private final NoteAccessValidator noteAccessValidator = new NoteAccessValidator(workspaceValidator, notebookRetrieveService, notebookAccessValidator);

    private NotebookDto notebook(String notebookId, NotebookVisibilityType visibility) {
        NotebookDto notebook = new NotebookDto();
        notebook.setNotebookId(notebookId);
        notebook.setWorkspaceId(WORKSPACE_ID);
        notebook.setOwnerId("notebook-owner");
        notebook.setVisibility(visibility);
        Mockito.when(notebookRetrieveService.retrieveOptional(notebookId)).thenReturn(Optional.of(notebook));
        return notebook;
    }

    private NoteDto note(String noteId, String ownerId, String notebookId) {
        NoteDto note = new NoteDto();
        note.setNoteId(noteId);
        note.setWorkspaceId(WORKSPACE_ID);
        note.setOwnerId(ownerId);
        note.setNotebookId(notebookId);
        return note;
    }

    private List<String> readableNoteIds(NoteDto... notes) {
        return noteAccessValidator.filterReadable(VIEWER, WORKSPACE_ID, List.of(notes))
                .stream()
                .map(NoteDto::getNoteId)
                .toList();
    }

    @Test
    void aNoteInAPrivateNotebookOfSomeoneElseIsLeftOut() {
        notebook("private-notebook", NotebookVisibilityType.PRIVATE);

        assertThat(readableNoteIds(note("note-1", "author", "private-notebook"))).isEmpty();
    }

    @Test
    void aSharedNotebookIsReadableOnlyForItsMembers() {
        notebook("shared-notebook", NotebookVisibilityType.SHARED);
        NoteDto note = note("note-1", "author", "shared-notebook");

        assertThat(readableNoteIds(note)).isEmpty();

        Mockito.when(notebookMemberRetrieveService.isNotebookMember(VIEWER, "shared-notebook")).thenReturn(true);
        assertThat(readableNoteIds(note)).containsExactly("note-1");
    }

    @Test
    void aWorkspacePublicNotebookIsReadableForWorkspaceMembers() {
        notebook("public-notebook", NotebookVisibilityType.PUBLIC_WITHIN_WORKSPACE);
        Mockito.when(workspaceValidator.isAccountWorkspaceMember(VIEWER, WORKSPACE_ID)).thenReturn(true);

        assertThat(readableNoteIds(note("note-1", "author", "public-notebook"))).containsExactly("note-1");
    }

    @Test
    void aDraftIsReadableOnlyForItsOwner() {
        assertThat(readableNoteIds(note("someone-elses-draft", "author", null), note("own-draft", VIEWER, null)))
                .containsExactly("own-draft");
    }

    @Test
    void aWorkspaceAdminReadsNotesInEveryNotebook() {
        notebook("private-notebook", NotebookVisibilityType.PRIVATE);
        Mockito.when(workspaceValidator.isWorkspaceAdminOrOwner(VIEWER, WORKSPACE_ID)).thenReturn(true);

        assertThat(readableNoteIds(note("note-1", "author", "private-notebook"))).containsExactly("note-1");
    }

    @Test
    void aNoteFromAnotherWorkspaceIsLeftOut() {
        NoteDto note = note("note-1", VIEWER, null);
        note.setWorkspaceId("workspace-2");

        assertThat(readableNoteIds(note)).isEmpty();
    }

    @Test
    void aNotebookThatNoLongerExistsHidesItsNotes() {
        Mockito.when(notebookRetrieveService.retrieveOptional("deleted-notebook")).thenReturn(Optional.empty());
        Mockito.when(workspaceValidator.isWorkspaceAdminOrOwner(VIEWER, WORKSPACE_ID)).thenReturn(true);

        assertThat(readableNoteIds(note("note-1", "author", "deleted-notebook"))).isEmpty();
    }

    @Test
    void eachNotebookIsCheckedOnce() {
        notebook("shared-notebook", NotebookVisibilityType.SHARED);
        Mockito.when(notebookMemberRetrieveService.isNotebookMember(VIEWER, "shared-notebook")).thenReturn(true);

        readableNoteIds(note("note-1", "author", "shared-notebook"), note("note-2", "author", "shared-notebook"));

        Mockito.verify(notebookRetrieveService, Mockito.times(1)).retrieveOptional("shared-notebook");
    }

    @Test
    void anAccountWithoutTaskAccessGetsNoNoteQuery() {
        SessionInfoService sessionInfoService = Mockito.mock(SessionInfoService.class);
        TaskRetrieveService taskRetrieveService = Mockito.mock(TaskRetrieveService.class);
        TeamMemberRetrieveService teamMemberRetrieveService = Mockito.mock(TeamMemberRetrieveService.class);
        NoteRetrieveService noteRetrieveService = Mockito.mock(NoteRetrieveService.class);
        TaskLinkingNoteManager manager = new TaskLinkingNoteManager(
                sessionInfoService, taskRetrieveService, workspaceValidator, teamMemberRetrieveService,
                Mockito.mock(TaskTeamVisibilityTypeAccessValidator.class), noteRetrieveService, noteAccessValidator,
                Mockito.mock(TaskLinkingNoteDtoConverter.class));
        TaskDto task = new TaskDto();
        task.setTaskId("task-1");
        task.setWorkspaceId(WORKSPACE_ID);
        task.setTeamId("team-1");
        Mockito.when(sessionInfoService.currentAccountId()).thenReturn(VIEWER);
        Mockito.when(taskRetrieveService.retrievePlain("task-1")).thenReturn(task);
        Mockito.when(teamMemberRetrieveService.retrieveMembership(WORKSPACE_ID, "team-1", VIEWER)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> manager.retrieveNotesLinkingTask("task-1")).isInstanceOf(NoAccessException.class);
        Mockito.verifyNoInteractions(noteRetrieveService);
    }
}
