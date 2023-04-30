package co.jinear.core.model.vo.task;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceActivityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotifyTaskSubscribersVo {

    private TaskDto taskDto;
    private WorkspaceActivityType workspaceActivityType;
}
