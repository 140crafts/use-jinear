package co.jinear.core.model.entity.task;

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
    private int page;
}
