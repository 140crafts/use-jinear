package co.jinear.core.model.vo.task;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceActivityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
@ToString
public class NotifyTaskSubscribersVo {

    private TaskDto taskDto;
    private WorkspaceActivityType workspaceActivityType;
    @Nullable
    private String performingAccountSessionId;
}
