package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.entity.topic.Topic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class TaskDto extends BaseDto {

    private String taskId;
    private String topicId;
    private String workspaceId;
    private String teamId;
    private String ownerId;
    private String workflowStatusId;
    private ZonedDateTime assignedDate;
    private ZonedDateTime dueDate;
    private Integer teamTagNo;
    private Integer topicTagNo;
    private String title;
    private String description;
    @Nullable
    private Topic topic;
    @Nullable
    private PlainAccountProfileDto owner;
    @Nullable
    private PlainAccountProfileDto assignedToAccount;
    @Nullable
    private WorkspaceDto workspace;
    @Nullable
    private TeamDto team;
    private TeamWorkflowStatusDto workflowStatus;
}
