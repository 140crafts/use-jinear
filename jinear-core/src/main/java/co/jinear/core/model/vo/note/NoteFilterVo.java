package co.jinear.core.model.vo.note;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoteFilterVo {

    private int page = 0;
    private String workspaceId;
    private String notebookId;
    private String parentNoteId;
    private String noteId;
    private String ownerId;
}
