package co.jinear.core.model.vo.note;

import co.jinear.core.model.vo.note.change.NoteBodyChange;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoteEditVo {

    private String workspaceId;
    private String noteId;
    private String title;
    @ToString.Exclude
    private NoteBodyChange bodyChange;
}
