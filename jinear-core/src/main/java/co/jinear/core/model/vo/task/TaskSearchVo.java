package co.jinear.core.model.vo.task;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskSearchVo {
    private String title;
    private String workspaceId;
    private String teamId;
    private String assignedTo;
    private String ownerId;
    private int page;
}
