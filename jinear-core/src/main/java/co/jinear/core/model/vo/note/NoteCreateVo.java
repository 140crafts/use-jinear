package co.jinear.core.model.vo.note;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoteCreateVo {

    private String workspaceId;
    private String notebookId;
    private String parentNoteId;
    private String title;
    @ToString.Exclude
    private String body;
}
