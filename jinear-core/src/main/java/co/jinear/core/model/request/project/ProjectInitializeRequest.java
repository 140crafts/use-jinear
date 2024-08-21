package co.jinear.core.model.request.project;

import co.jinear.core.model.enumtype.project.ProjectPriorityType;
import co.jinear.core.model.enumtype.project.ProjectStateType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class ProjectInitializeRequest extends BaseRequest {

    @NotBlank
    private String workspaceId;
    @NotBlank
    private String title;
    @Nullable
    private String description;
    @Nullable
    private ProjectStateType projectState = ProjectStateType.BACKLOG;
    @Nullable
    private ProjectPriorityType projectPriority = ProjectPriorityType.LOW;
    @Nullable
    private String leadWorkspaceMemberId;
    @Nullable
    private ZonedDateTime startDate;
    @Nullable
    private ZonedDateTime targetDate;
    @ToString.Exclude
    private List<String> teamIds;
}
