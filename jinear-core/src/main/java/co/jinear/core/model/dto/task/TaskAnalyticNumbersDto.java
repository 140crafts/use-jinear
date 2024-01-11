package co.jinear.core.model.dto.task;

import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class TaskAnalyticNumbersDto {

    private Long missedDeadlineCount;
    private Long deadlineComingUpCount;
    private Long totalOpenTaskCount;
    private Long totalClosedTaskCount;
    private Long totalTaskCount;
    private Map<TeamWorkflowStateGroup, Long> statusCounts;
}
