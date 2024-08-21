package co.jinear.core.model.vo.project;

import co.jinear.core.model.enumtype.project.ProjectPriorityType;
import co.jinear.core.model.enumtype.project.ProjectStateType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class ProjectInitializeVo {

    private String workspaceId;
    private String title;
    private String description;
    private ProjectStateType projectState;
    private ProjectPriorityType projectPriority;
    private String leadWorkspaceMemberId;
    private ZonedDateTime startDate;
    private ZonedDateTime targetDate;
    @ToString.Exclude
    private List<String> teamIds;
}
