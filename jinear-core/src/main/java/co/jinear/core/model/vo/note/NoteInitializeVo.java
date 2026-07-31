package co.jinear.core.model.vo.note;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoteInitializeVo {

    private String conversationId;
    private String notebookId;
    private String workspaceId;
    private String ownerId;
    private String parentNoteId;
    private String title;
    @ToString.Exclude
    private String bodyState;
}
