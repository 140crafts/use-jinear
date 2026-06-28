package co.jinear.core.model.vo.notetag;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoteTagInitializeVo {

    private String notebookId;
    private String workspaceId;
    private String name;
    private String color;
}
