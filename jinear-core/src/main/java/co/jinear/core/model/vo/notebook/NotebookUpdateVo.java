package co.jinear.core.model.vo.notebook;

import co.jinear.core.model.enumtype.notebook.NotebookVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotebookUpdateVo {

    private String notebookId;
    private String title;
    private String description;
    private NotebookVisibilityType visibility;
}
