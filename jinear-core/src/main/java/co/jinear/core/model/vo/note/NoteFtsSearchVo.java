package co.jinear.core.model.vo.note;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoteFtsSearchVo {

    private String query;
    private String workspaceId;
    private String notebookId;
    private String parentNoteId;
    private String accountId;
    private boolean workspaceAdmin;
    private int page;
}
