package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@ToString(callSuper = true)
public class TaskSearchResultDto extends BaseDto {
    private String taskId;
    private String topicId;
    private String workspaceId;
    private String teamId;
    private String ownerId;
    private String workflowStatusId;
    private String assignedTo;
    private ZonedDateTime assignedDate;
    private ZonedDateTime dueDate;
    private Boolean hasPreciseAssignedDate;
    private Boolean hasPreciseDueDate;
    private Integer teamTagNo;
    private Integer topicTagNo;
    private String title;
    private String teamTag;
    private String workflowStateName;
    private TeamWorkflowStateGroup workflowStateGroup;
}
