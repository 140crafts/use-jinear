package co.jinear.core.manager.workspace;

import co.jinear.core.converter.workspace.WorkspacePrefetchRequestConverter;
import co.jinear.core.manager.calendar.CalendarEventManager;
import co.jinear.core.manager.calendar.CalendarMemberManager;
import co.jinear.core.manager.material.MaterialListingManager;
import co.jinear.core.manager.note.NoteFilterManager;
import co.jinear.core.manager.notebook.NotebookListingManager;
import co.jinear.core.manager.notification.NotificationEventListingManager;
import co.jinear.core.manager.task.TaskListingManager;
import co.jinear.core.manager.task.TaskMediaManager;
import co.jinear.core.manager.team.TeamMemberManager;
import co.jinear.core.manager.team.TeamRetrieveManager;
import co.jinear.core.manager.team.TeamWorkflowStatusManager;
import co.jinear.core.manager.topic.TopicListingManager;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.dto.workspace.WorkspacePrefetchDto;
import co.jinear.core.model.dto.workspace.WorkspacePrefetchNotebookDto;
import co.jinear.core.model.dto.workspace.WorkspacePrefetchTeamDto;
import co.jinear.core.model.request.calendar.CalendarEventFilterRequest;
import co.jinear.core.model.request.material.MaterialSearchRequest;
import co.jinear.core.model.request.note.NoteFilterRequest;
import co.jinear.core.model.request.task.TaskFilterRequest;
import co.jinear.core.model.request.workspace.WorkspaceActivityFilterRequest;
import co.jinear.core.model.request.workspace.WorkspacePrefetchMaterialFilterRequest;
import co.jinear.core.model.request.workspace.WorkspacePrefetchRequest;
import co.jinear.core.model.request.workspace.WorkspacePrefetchTimespanRequest;
import co.jinear.core.model.response.calendar.CalendarEventListingResponse;
import co.jinear.core.model.response.calendar.CalendarMemberListingResponse;
import co.jinear.core.model.response.material.ParentMaterialDtoResponse;
import co.jinear.core.model.response.note.NotePaginatedResponse;
import co.jinear.core.model.response.notebook.NotebookListingResponse;
import co.jinear.core.model.response.notification.NotificationEventListingResponse;
import co.jinear.core.model.response.notification.RetrieveUnreadNotificationEventCountResponse;
import co.jinear.core.model.response.task.TaskListingPaginatedResponse;
import co.jinear.core.model.response.task.TaskPaginatedMediaResponse;
import co.jinear.core.model.response.team.TeamListingResponse;
import co.jinear.core.model.response.team.TeamMemberListingResponse;
import co.jinear.core.model.response.team.TeamMembershipsResponse;
import co.jinear.core.model.response.team.TeamWorkflowStatusListingResponse;
import co.jinear.core.model.response.topic.TopicSearchResponse;
import co.jinear.core.model.response.workspace.WorkspaceActivityListResponse;
import co.jinear.core.model.response.workspace.WorkspaceMediaLimitResponse;
import co.jinear.core.model.response.workspace.WorkspacePrefetchResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspacePrefetchManager {

    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final WorkspacePrefetchRequestConverter workspacePrefetchRequestConverter;
    private final TeamRetrieveManager teamRetrieveManager;
    private final TeamMemberManager teamMemberManager;
    private final WorkspaceActivityFilterManager workspaceActivityFilterManager;
    private final TaskListingManager taskListingManager;
    private final NotificationEventListingManager notificationEventListingManager;
    private final CalendarMemberManager calendarMemberManager;
    private final WorkspaceMediaManager workspaceMediaManager;
    private final CalendarEventManager calendarEventManager;
    private final MaterialListingManager materialListingManager;
    private final TeamWorkflowStatusManager teamWorkflowStatusManager;
    private final TaskMediaManager taskMediaManager;
    private final TopicListingManager topicListingManager;
    private final NotebookListingManager notebookListingManager;
    private final NoteFilterManager noteFilterManager;

    public WorkspacePrefetchResponse prefetch(WorkspacePrefetchRequest request) {
        String currentAccountId = sessionInfoService.currentAccountId();
        String workspaceId = request.getWorkspaceId();
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        log.info("Workspace prefetch has started. accountId: {}, workspaceId: {}", currentAccountId, workspaceId);

        WorkspacePrefetchDto workspacePrefetchDto = new WorkspacePrefetchDto();
        populateWorkspaceTeams(workspacePrefetchDto, workspaceId);
        populateWorkspaceActivities(workspacePrefetchDto, workspaceId);
        populateAssignedTasks(workspacePrefetchDto, workspaceId, currentAccountId);
        populateNotifications(workspacePrefetchDto, workspaceId);
        populateCalendar(workspacePrefetchDto, request);
        populateWorkspaceMediaLimits(workspacePrefetchDto, workspaceId);
        populateMaterials(workspacePrefetchDto, request);
        populateTeams(workspacePrefetchDto, request);
        populateNotebooks(workspacePrefetchDto, workspaceId);
        return mapResponse(workspacePrefetchDto);
    }

    private void populateWorkspaceTeams(WorkspacePrefetchDto workspacePrefetchDto, String workspaceId) {
        TeamListingResponse workspaceTeams = teamRetrieveManager.retrieveWorkspaceTeams(workspaceId);
        TeamMembershipsResponse teamMemberships = teamMemberManager.retrieveMemberships(workspaceId);
        workspacePrefetchDto.setWorkspaceTeams(workspaceTeams);
        workspacePrefetchDto.setTeamMemberships(teamMemberships);
    }

    private void populateWorkspaceActivities(WorkspacePrefetchDto workspacePrefetchDto, String workspaceId) {
        WorkspaceActivityFilterRequest workspaceActivityFilterRequest = workspacePrefetchRequestConverter.mapActivityFilter(workspaceId);
        WorkspaceActivityListResponse workspaceActivities = workspaceActivityFilterManager.filterActivities(workspaceActivityFilterRequest);
        workspacePrefetchDto.setWorkspaceActivities(workspaceActivities);
    }

    private void populateAssignedTasks(WorkspacePrefetchDto workspacePrefetchDto, String workspaceId, String currentAccountId) {
        TaskFilterRequest taskFilterRequest = workspacePrefetchRequestConverter.mapAssignedTaskFilter(workspaceId, currentAccountId);
        TaskListingPaginatedResponse assignedTasks = taskListingManager.filterTasks(taskFilterRequest);
        workspacePrefetchDto.setAssignedTasks(assignedTasks);
    }

    private void populateNotifications(WorkspacePrefetchDto workspacePrefetchDto, String workspaceId) {
        NotificationEventListingResponse notifications = notificationEventListingManager.retrieveNotifications(workspaceId, 0);
        RetrieveUnreadNotificationEventCountResponse unreadNotificationCount = notificationEventListingManager.retrieveUnreadNotificationCount(workspaceId);
        workspacePrefetchDto.setNotifications(notifications);
        workspacePrefetchDto.setUnreadNotificationCount(unreadNotificationCount);
    }

    private void populateCalendar(WorkspacePrefetchDto workspacePrefetchDto, WorkspacePrefetchRequest request) {
        CalendarMemberListingResponse calendarMemberships = calendarMemberManager.retrieveMemberCalendars(request.getWorkspaceId());
        List<CalendarEventListingResponse> calendarEvents = new ArrayList<>();
        for (WorkspacePrefetchTimespanRequest timespan : request.getCalendarTimespans()) {
            CalendarEventListingResponse calendarEventListingResponse = retrieveCalendarEvents(request.getWorkspaceId(), timespan);
            calendarEvents.add(calendarEventListingResponse);
        }
        workspacePrefetchDto.setCalendarMemberships(calendarMemberships);
        workspacePrefetchDto.setCalendarEvents(calendarEvents);
    }

    private CalendarEventListingResponse retrieveCalendarEvents(String workspaceId, WorkspacePrefetchTimespanRequest timespan) {
        CalendarEventFilterRequest calendarEventFilterRequest = workspacePrefetchRequestConverter.mapCalendarEventFilter(workspaceId, timespan);
        return calendarEventManager.filterCalendarEvents(calendarEventFilterRequest);
    }

    private void populateWorkspaceMediaLimits(WorkspacePrefetchDto workspacePrefetchDto, String workspaceId) {
        WorkspaceMediaLimitResponse workspaceMediaLimits = workspaceMediaManager.retrieveLimits(workspaceId);
        workspacePrefetchDto.setWorkspaceMediaLimits(workspaceMediaLimits);
    }

    private void populateMaterials(WorkspacePrefetchDto workspacePrefetchDto, WorkspacePrefetchRequest request) {
        List<ParentMaterialDtoResponse> materials = new ArrayList<>();
        for (WorkspacePrefetchMaterialFilterRequest materialFilter : request.getMaterialFilters()) {
            ParentMaterialDtoResponse parentMaterialDtoResponse = searchMaterials(request.getWorkspaceId(), materialFilter);
            materials.add(parentMaterialDtoResponse);
        }
        workspacePrefetchDto.setMaterials(materials);
    }

    private ParentMaterialDtoResponse searchMaterials(String workspaceId, WorkspacePrefetchMaterialFilterRequest materialFilter) {
        MaterialSearchRequest materialSearchRequest = workspacePrefetchRequestConverter.mapMaterialSearch(workspaceId, materialFilter);
        return materialListingManager.search(materialSearchRequest);
    }

    private void populateTeams(WorkspacePrefetchDto workspacePrefetchDto, WorkspacePrefetchRequest request) {
        List<TeamMemberDto> memberships = workspacePrefetchDto.getTeamMemberships().getTeamMemberDtoList();
        List<WorkspacePrefetchTeamDto> teams = new ArrayList<>();
        for (TeamMemberDto membership : memberships) {
            if (isTeamLimitReached(request, teams)) {
                break;
            }
            WorkspacePrefetchTeamDto workspacePrefetchTeamDto = new WorkspacePrefetchTeamDto();
            populateTeam(workspacePrefetchTeamDto, request, membership.getTeamId());
            teams.add(workspacePrefetchTeamDto);
        }
        workspacePrefetchDto.setTeams(teams);
    }

    private boolean isTeamLimitReached(WorkspacePrefetchRequest request, List<WorkspacePrefetchTeamDto> teams) {
        Integer teamLimit = request.getTeamLimit();
        return Objects.nonNull(teamLimit) && teams.size() >= teamLimit;
    }

    private void populateTeam(WorkspacePrefetchTeamDto workspacePrefetchTeamDto, WorkspacePrefetchRequest request, String teamId) {
        TaskFilterRequest taskFilterRequest = workspacePrefetchRequestConverter.mapTeamTaskFilter(request.getWorkspaceId(), teamId);
        TaskListingPaginatedResponse tasks = taskListingManager.filterTasks(taskFilterRequest);
        TeamWorkflowStatusListingResponse workflowStatuses = teamWorkflowStatusManager.retrieveAllFromTeam(teamId);
        TeamMemberListingResponse members = teamMemberManager.retrieveTeamMembers(teamId, 0);
        TopicSearchResponse topics = topicListingManager.searchTeamTopics(teamId, "");
        workspacePrefetchTeamDto.setTeamId(teamId);
        workspacePrefetchTeamDto.setTasks(tasks);
        workspacePrefetchTeamDto.setWorkflowStatuses(workflowStatuses);
        workspacePrefetchTeamDto.setMembers(members);
        workspacePrefetchTeamDto.setTopics(topics);
        if (request.isIncludeTeamTaskMedia()) {
            TaskPaginatedMediaResponse taskMedia = taskMediaManager.retrieveTaskMediaListFromTeam(teamId, 0);
            workspacePrefetchTeamDto.setTaskMedia(taskMedia);
        }
    }

    private void populateNotebooks(WorkspacePrefetchDto workspacePrefetchDto, String workspaceId) {
        NotebookListingResponse notebooks = notebookListingManager.listWorkspaceNotebooks(workspaceId, 0);
        NoteFilterRequest draftNoteFilterRequest = workspacePrefetchRequestConverter.mapNoteFilter(workspaceId, null);
        NotePaginatedResponse draftNotes = noteFilterManager.filter(draftNoteFilterRequest);
        workspacePrefetchDto.setNotebooks(notebooks);
        workspacePrefetchDto.setDraftNotes(draftNotes);
        populateNotebookNotesUntilFirstNote(workspacePrefetchDto, workspaceId);
    }

    private void populateNotebookNotesUntilFirstNote(WorkspacePrefetchDto workspacePrefetchDto, String workspaceId) {
        List<NotebookDto> notebookDtos = workspacePrefetchDto.getNotebooks().getNotebookDtoPageDto().getContent();
        List<WorkspacePrefetchNotebookDto> notebookNotes = new ArrayList<>();
        for (NotebookDto notebookDto : notebookDtos) {
            WorkspacePrefetchNotebookDto workspacePrefetchNotebookDto = new WorkspacePrefetchNotebookDto();
            populateNotebookNotes(workspacePrefetchNotebookDto, workspaceId, notebookDto.getNotebookId());
            notebookNotes.add(workspacePrefetchNotebookDto);
            if (hasNotes(workspacePrefetchNotebookDto)) {
                break;
            }
        }
        workspacePrefetchDto.setNotebookNotes(notebookNotes);
    }

    private void populateNotebookNotes(WorkspacePrefetchNotebookDto workspacePrefetchNotebookDto, String workspaceId, String notebookId) {
        NoteFilterRequest noteFilterRequest = workspacePrefetchRequestConverter.mapNoteFilter(workspaceId, notebookId);
        NotePaginatedResponse notes = noteFilterManager.filter(noteFilterRequest);
        workspacePrefetchNotebookDto.setNotebookId(notebookId);
        workspacePrefetchNotebookDto.setNotes(notes);
    }

    private boolean hasNotes(WorkspacePrefetchNotebookDto workspacePrefetchNotebookDto) {
        List<NoteDto> notes = workspacePrefetchNotebookDto.getNotes().getNoteDtoPageDto().getContent();
        return !notes.isEmpty();
    }

    private WorkspacePrefetchResponse mapResponse(WorkspacePrefetchDto workspacePrefetchDto) {
        WorkspacePrefetchResponse workspacePrefetchResponse = new WorkspacePrefetchResponse();
        workspacePrefetchResponse.setWorkspacePrefetchDto(workspacePrefetchDto);
        return workspacePrefetchResponse;
    }
}
