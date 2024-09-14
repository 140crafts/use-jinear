package co.jinear.core.model.dto.project;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.enumtype.project.ProjectPriorityType;
import co.jinear.core.model.enumtype.project.ProjectStateType;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Set;

@Getter
@Setter
public class ProjectDto extends BaseDto {

    private String projectId;
    private String workspaceId;
    private String title;
    private String descriptionRichTextId;
    private ProjectStateType projectState;
    private ProjectPriorityType projectPriority;
    private String leadWorkspaceMemberId;
    private ZonedDateTime startDate;
    private ZonedDateTime targetDate;
    private WorkspaceMemberDto leadWorkspaceMember;
    private RichTextDto description;
    private Set<ProjectTeamDto> projectTeams;
    private Set<MilestoneDto> milestones;
    private WorkspaceDto workspace;
}
