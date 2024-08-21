package co.jinear.core.model.request.task;

import co.jinear.core.model.dto.calendar.TaskExternalCalendarFilterDto;
import co.jinear.core.model.enumtype.FilterSort;
import co.jinear.core.model.enumtype.team.TeamWorkflowStateGroup;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.ZonedDateTime;
import java.util.List;

import static co.jinear.core.model.enumtype.FilterSort.IDATE_DESC;

@Getter
@Setter
@ToString
public class TaskFilterRequest extends BaseRequest {

    @Nullable
    private Integer page = 0;
    @Min(1)
    @Max(250)
    @Nullable
    private Integer size = 250;
    @NotBlank
    private String workspaceId;
    @Nullable
    private List<String> teamIdList;
    @Nullable
    private List<String> excludingTeamIdList;
    @Nullable
    private List<String> topicIds;
    @Nullable
    private List<String> ownerIds;
    @Nullable
    private List<String> assigneeIds;
    @Nullable
    private List<String> workflowStatusIdList;
    @Nullable
    private List<TeamWorkflowStateGroup> workflowStateGroups;
    @Nullable
    private ZonedDateTime timespanStart;
    @Nullable
    private ZonedDateTime timespanEnd;
    @Nullable
    private FilterSort sort = IDATE_DESC;
    @Nullable
    private List<TaskExternalCalendarFilterDto> externalCalendarList;
    @Nullable
    private List<String> projectIds;
    @Nullable
    private List<String> milestoneIds;
}
