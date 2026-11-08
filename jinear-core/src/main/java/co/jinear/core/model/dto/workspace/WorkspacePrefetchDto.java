package co.jinear.core.model.dto.workspace;

import co.jinear.core.model.response.calendar.CalendarEventListingResponse;
import co.jinear.core.model.response.calendar.CalendarMemberListingResponse;
import co.jinear.core.model.response.material.ParentMaterialDtoResponse;
import co.jinear.core.model.response.note.NotePaginatedResponse;
import co.jinear.core.model.response.notebook.NotebookListingResponse;
import co.jinear.core.model.response.notification.NotificationEventListingResponse;
import co.jinear.core.model.response.notification.RetrieveUnreadNotificationEventCountResponse;
import co.jinear.core.model.response.task.TaskListingPaginatedResponse;
import co.jinear.core.model.response.team.TeamListingResponse;
import co.jinear.core.model.response.team.TeamMembershipsResponse;
import co.jinear.core.model.response.workspace.WorkspaceActivityListResponse;
import co.jinear.core.model.response.workspace.WorkspaceMediaLimitResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WorkspacePrefetchDto {

    private TeamListingResponse workspaceTeams;
    private TeamMembershipsResponse teamMemberships;
    private WorkspaceActivityListResponse workspaceActivities;
    private TaskListingPaginatedResponse assignedTasks;
    private NotificationEventListingResponse notifications;
    private RetrieveUnreadNotificationEventCountResponse unreadNotificationCount;
    private CalendarMemberListingResponse calendarMemberships;
    private WorkspaceMediaLimitResponse workspaceMediaLimits;
    private NotebookListingResponse notebooks;
    private NotePaginatedResponse draftNotes;
    private List<CalendarEventListingResponse> calendarEvents;
    private List<ParentMaterialDtoResponse> materials;
    private List<WorkspacePrefetchTeamDto> teams;
    private List<WorkspacePrefetchNotebookDto> notebookNotes;
}
