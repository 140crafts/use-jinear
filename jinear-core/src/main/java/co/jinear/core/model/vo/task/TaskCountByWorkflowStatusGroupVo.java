package co.jinear.core.model.vo.task;

import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCountByWorkflowStatusGroupVo {

    private TeamWorkflowStateGroup workflowStateGroup;
    private Long taskCount;

    public TaskCountByWorkflowStatusGroupVo() {
    }

    public TaskCountByWorkflowStatusGroupVo(TeamWorkflowStateGroup workflowStateGroup, Long taskCount) {
        this.workflowStateGroup = workflowStateGroup;
        this.taskCount = taskCount;
    }
}
