package co.jinear.core.model.dto.workspace;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.dto.project.MilestoneDto;
import co.jinear.core.model.dto.project.ProjectDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.*;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceActivityType;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class WorkspaceActivityDto extends BaseDto {

    private String workspaceActivityId;
    private String workspaceId;
    private String teamId;
    private String taskId;
    private WorkspaceActivityType type;
    private String performedBy;
    private String relatedObjectId;
    @Nullable
    private String groupId;
    @Nullable
    private String groupTitle;
    @Nullable
    private String groupLink;
    @Nullable
    private String oldState;
    @Nullable
    private String newState;
    private PlainAccountProfileDto performedByAccount;
    @Nullable
    private WorkspaceDto workspaceDto;
    @Nullable
    private TeamDto teamDto;
    @Nullable
    private PlainAccountProfileDto relatedAccount;
    @Nullable
    private RichTextDto oldDescription;
    @Nullable
    private RichTextDto newDescription;
    @Nullable
    private TeamWorkflowStatusDto oldWorkflowStatusDto;
    @Nullable
    private TeamWorkflowStatusDto newWorkflowStatusDto;
    @Nullable
    private TopicDto oldTopicDto;
    @Nullable
    private TopicDto newTopicDto;
    @Nullable
    private PlainAccountProfileDto oldAssignedToAccount;
    @Nullable
    private PlainAccountProfileDto newAssignedToAccount;
    @Nullable
    private TaskRelationDto oldTaskRelationDto;
    @Nullable
    private TaskRelationDto newTaskRelationDto;
    @Nullable
    private ChecklistDto relatedChecklist;
    @Nullable
    private ChecklistItemDto relatedChecklistItem;
    @Nullable
    private TaskDto relatedTask;
    @Nullable
    private TaskBoardDto taskBoard;
    @Nullable
    private MediaDto relatedTaskMedia;
    @Nullable
    private ProjectDto oldProject;
    @Nullable
    private ProjectDto newProject;
    @Nullable
    private MilestoneDto oldMilestoneDto;
    @Nullable
    private MilestoneDto newMilestoneDto;
}
