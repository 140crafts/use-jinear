package co.jinear.core.model.dto.workspace;

import co.jinear.core.model.response.task.TaskListingPaginatedResponse;
import co.jinear.core.model.response.task.TaskPaginatedMediaResponse;
import co.jinear.core.model.response.team.TeamMemberListingResponse;
import co.jinear.core.model.response.team.TeamWorkflowStatusListingResponse;
import co.jinear.core.model.response.topic.TopicSearchResponse;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class WorkspacePrefetchTeamDto {

    private String teamId;
    private TaskListingPaginatedResponse tasks;
    private TeamWorkflowStatusListingResponse workflowStatuses;
    @Nullable
    private TaskPaginatedMediaResponse taskMedia;
    private TeamMemberListingResponse members;
    private TopicSearchResponse topics;
}
