package co.jinear.core.model.vo.notebook;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddNotebookMemberVo {

    private String accountId;
    private String workspaceId;
    private String notebookId;
}
