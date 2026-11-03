package co.jinear.core.converter.note;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.entity.note.Note;
import co.jinear.core.model.entity.username.Username;
import co.jinear.core.model.entity.workspace.Workspace;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NoteUrlConverter {

    // Must match DRAFTS_NOTEBOOK_ID in jinear-app: the app routes notes without a notebook under this id.
    private static final String DRAFTS_NOTEBOOK_ID = "drafts";
    private static final String TASK_PATH = "/{workspaceName}/task/{taskTag}";
    private static final String NOTE_PATH = "/{workspaceName}/notebook/{notebookId}/note/{noteId}";

    private final FeProperties feProperties;

    @AfterMapping
    public void fillUrl(Note note, @MappingTarget NoteDto noteDto) {
        String workspaceUsername = Optional.of(note)
                .map(Note::getWorkspace)
                .map(Workspace::getUsername)
                .map(Username::getUsername)
                .orElse(null);
        String url = noteUrl(workspaceUsername, note.getNotebookId(), note.getNoteId());
        noteDto.setUrl(url);
    }

    public String noteUrl(String workspaceUsername, String notebookId, String noteId) {
        String template = template();
        if (StringUtils.isBlank(template) || StringUtils.isBlank(workspaceUsername)) {
            return null;
        }
        String notebookReference = Optional.ofNullable(notebookId).orElse(DRAFTS_NOTEBOOK_ID);
        return template
                .replace("{workspaceName}", workspaceUsername)
                .replace("{notebookId}", notebookReference)
                .replace("{noteId}", noteId);
    }

    private String template() {
        return Optional.ofNullable(feProperties.getNoteUrl())
                .filter(StringUtils::isNotBlank)
                .orElseGet(this::templateFromTaskUrl);
    }

    // Installs configured before fe.note-url existed keep their files on upgrade, so the note link is derived from fe.task-url.
    private String templateFromTaskUrl() {
        String taskUrl = Optional.ofNullable(feProperties.getTaskUrl()).orElse("");
        if (!taskUrl.endsWith(TASK_PATH)) {
            return null;
        }
        return StringUtils.removeEnd(taskUrl, TASK_PATH) + NOTE_PATH;
    }
}
