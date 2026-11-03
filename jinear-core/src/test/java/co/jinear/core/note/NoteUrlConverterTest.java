package co.jinear.core.note;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.converter.note.NoteUrlConverter;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.entity.note.Note;
import co.jinear.core.model.entity.username.Username;
import co.jinear.core.model.entity.workspace.Workspace;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoteUrlConverterTest {

    private static final String TEMPLATE = "https://app.jinear.co/{workspaceName}/notebook/{notebookId}/note/{noteId}";

    @Test
    void aNotebookNoteLinksToItsNotebook() {
        NoteDto noteDto = convert(TEMPLATE, note("nb-1", "acme"));

        assertThat(noteDto.getUrl()).isEqualTo("https://app.jinear.co/acme/notebook/nb-1/note/note-1");
    }

    @Test
    void aDraftLinksToTheDraftsNotebook() {
        NoteDto noteDto = convert(TEMPLATE, note(null, "acme"));

        assertThat(noteDto.getUrl()).isEqualTo("https://app.jinear.co/acme/notebook/drafts/note/note-1");
    }

    @Test
    void withoutAWorkspaceUsernameThereIsNoUrl() {
        NoteDto noteDto = convert(TEMPLATE, note("nb-1", null));

        assertThat(noteDto.getUrl()).isNull();
    }

    @Test
    void withoutANoteTemplateTheLinkIsDerivedFromTheTaskTemplate() {
        NoteDto noteDto = convert("", "https://app.jinear.co/{workspaceName}/task/{taskTag}", note("nb-1", "acme"));

        assertThat(noteDto.getUrl()).isEqualTo("https://app.jinear.co/acme/notebook/nb-1/note/note-1");
    }

    @Test
    void aTaskTemplateOfAnotherShapeGivesNoUrl() {
        NoteDto noteDto = convert("", "https://app.jinear.co/tasks?tag={taskTag}", note("nb-1", "acme"));

        assertThat(noteDto.getUrl()).isNull();
    }

    @Test
    void withoutAnyTemplateThereIsNoUrl() {
        NoteDto noteDto = convert("", null, note("nb-1", "acme"));

        assertThat(noteDto.getUrl()).isNull();
    }

    private NoteDto convert(String template, Note note) {
        return convert(template, null, note);
    }

    private NoteDto convert(String template, String taskTemplate, Note note) {
        FeProperties feProperties = new FeProperties();
        feProperties.setNoteUrl(template);
        feProperties.setTaskUrl(taskTemplate);
        NoteDto noteDto = new NoteDto();
        new NoteUrlConverter(feProperties).fillUrl(note, noteDto);
        return noteDto;
    }

    private Note note(String notebookId, String workspaceUsername) {
        Note note = new Note();
        note.setNoteId("note-1");
        note.setNotebookId(notebookId);
        if (workspaceUsername != null) {
            Username username = new Username();
            username.setUsername(workspaceUsername);
            Workspace workspace = new Workspace();
            workspace.setUsername(username);
            note.setWorkspace(workspace);
        }
        return note;
    }
}
