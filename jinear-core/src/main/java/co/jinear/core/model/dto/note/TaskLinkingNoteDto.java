package co.jinear.core.model.dto.note;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskLinkingNoteDto {

    private String noteId;
    private String workspaceId;
    private String notebookId;
    private String notebookTitle;
    private String title;
}
