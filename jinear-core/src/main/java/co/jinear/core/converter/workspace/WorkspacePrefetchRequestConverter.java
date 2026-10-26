package co.jinear.core.converter.workspace;

import co.jinear.core.model.request.calendar.CalendarEventFilterRequest;
import co.jinear.core.model.request.material.MaterialSearchRequest;
import co.jinear.core.model.request.note.NoteFilterRequest;
import co.jinear.core.model.request.task.TaskFilterRequest;
import co.jinear.core.model.request.workspace.WorkspaceActivityFilterRequest;
import co.jinear.core.model.request.workspace.WorkspacePrefetchMaterialFilterRequest;
import co.jinear.core.model.request.workspace.WorkspacePrefetchTimespanRequest;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkspacePrefetchRequestConverter {

    public WorkspaceActivityFilterRequest mapActivityFilter(String workspaceId) {
        WorkspaceActivityFilterRequest workspaceActivityFilterRequest = new WorkspaceActivityFilterRequest();
        workspaceActivityFilterRequest.setWorkspaceId(workspaceId);
        return workspaceActivityFilterRequest;
    }

    public TaskFilterRequest mapAssignedTaskFilter(String workspaceId, String accountId) {
        TaskFilterRequest taskFilterRequest = new TaskFilterRequest();
        taskFilterRequest.setWorkspaceId(workspaceId);
        taskFilterRequest.setAssigneeIds(List.of(accountId));
        return taskFilterRequest;
    }

    public TaskFilterRequest mapTeamTaskFilter(String workspaceId, String teamId) {
        TaskFilterRequest taskFilterRequest = new TaskFilterRequest();
        taskFilterRequest.setWorkspaceId(workspaceId);
        taskFilterRequest.setTeamIdList(List.of(teamId));
        return taskFilterRequest;
    }

    public CalendarEventFilterRequest mapCalendarEventFilter(String workspaceId, WorkspacePrefetchTimespanRequest timespan) {
        CalendarEventFilterRequest calendarEventFilterRequest = new CalendarEventFilterRequest();
        calendarEventFilterRequest.setWorkspaceId(workspaceId);
        calendarEventFilterRequest.setTaskboardIds(List.of());
        calendarEventFilterRequest.setAssigneeIds(List.of());
        calendarEventFilterRequest.setCollaboratorIds(List.of());
        calendarEventFilterRequest.setWorkflowStatusIdList(List.of());
        calendarEventFilterRequest.setTimespanStart(timespan.getTimespanStart());
        calendarEventFilterRequest.setTimespanEnd(timespan.getTimespanEnd());
        return calendarEventFilterRequest;
    }

    public NoteFilterRequest mapNoteFilter(String workspaceId, @Nullable String notebookId) {
        NoteFilterRequest noteFilterRequest = new NoteFilterRequest();
        noteFilterRequest.setWorkspaceId(workspaceId);
        noteFilterRequest.setNotebookId(notebookId);
        return noteFilterRequest;
    }

    public MaterialSearchRequest mapMaterialSearch(String workspaceId, WorkspacePrefetchMaterialFilterRequest materialFilter) {
        MaterialSearchRequest materialSearchRequest = new MaterialSearchRequest();
        materialSearchRequest.setWorkspaceId(workspaceId);
        materialSearchRequest.setMaterialSearchSortType(materialFilter.getMaterialSearchSortType());
        materialSearchRequest.setMaterialSearchContentFilterType(materialFilter.getMaterialSearchContentFilterType());
        return materialSearchRequest;
    }
}
