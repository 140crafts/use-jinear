package co.jinear.core.model.dto.task;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;
import java.util.Set;

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
    private String assignedTo;
    private ZonedDateTime assignedDate;
    private ZonedDateTime dueDate;
    private Boolean hasPreciseAssignedDate;
    private Boolean hasPreciseDueDate;
    private Integer teamTagNo;
    private Integer topicTagNo;
    private String title;
    @Nullable
    private RichTextDto description;
    @Nullable
    private TopicDto topic;
    @Nullable
    private PlainAccountProfileDto owner;
    @Nullable
    private PlainAccountProfileDto assignedToAccount;
    @Nullable
    private WorkspaceDto workspace;
    @Nullable
    private TeamDto team;
    private TeamWorkflowStatusDto workflowStatus;
    @Nullable
    private Set<TaskRelationDto> relations;
    @Nullable
    private Set<TaskRelationDto> relatedIn;
    @Nullable
    private Set<TaskReminderDto> taskReminders;
    @Nullable
    private Set<ChecklistDto> checklists;
}
