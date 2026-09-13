package co.jinear.core.converter.mcp;

import co.jinear.core.model.dto.calendar.CalendarEventDto;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.CommentDto;
import co.jinear.core.model.dto.task.TaskBoardDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.mcp.view.McpBoardView;
import co.jinear.core.model.mcp.view.McpCalendarEventView;
import co.jinear.core.model.mcp.view.McpCommentView;
import co.jinear.core.model.mcp.view.McpFileView;
import co.jinear.core.model.mcp.view.McpMemberView;
import co.jinear.core.model.mcp.view.McpNoteDetailView;
import co.jinear.core.model.mcp.view.McpNoteView;
import co.jinear.core.model.mcp.view.McpNotebookView;
import co.jinear.core.model.mcp.view.McpTaskDetailView;
import co.jinear.core.model.mcp.view.McpTaskView;
import co.jinear.core.model.mcp.view.McpTeamView;
import co.jinear.core.model.mcp.view.McpTopicView;
import co.jinear.core.model.mcp.view.McpWorkflowStatusView;
import co.jinear.core.model.mcp.view.McpWorkspaceMembershipView;
import co.jinear.core.model.mcp.view.McpWorkspaceView;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 * Maps Jinear DTOs onto the payload views MCP tools return. Every published field lives on a
 * view class, so the JSON Schema an MCP client reads is generated from the same declaration
 * this converter fills in.
 */
@Component
public class McpViewConverter {

    public McpWorkspaceView workspace(WorkspaceDto dto) {
        McpWorkspaceView view = new McpWorkspaceView();
        fillWorkspace(view, dto);
        return view;
    }

    public McpWorkspaceMembershipView workspaceMembership(DetailedWorkspaceMemberDto dto) {
        McpWorkspaceMembershipView view = new McpWorkspaceMembershipView();
        fillWorkspace(view, dto.getWorkspace());
        view.setRole(name(dto.getRole()));
        return view;
    }

    public McpMemberView member(WorkspaceMemberDto dto) {
        McpMemberView view = new McpMemberView();
        view.setWorkspaceMemberId(dto.getWorkspaceMemberId());
        view.setAccountId(dto.getAccountId());
        view.setRole(name(dto.getRole()));
        if (Objects.nonNull(dto.getAccount())) {
            view.setUsername(dto.getAccount().getUsername());
            view.setEmail(dto.getAccount().getEmail());
        }
        return view;
    }

    public McpTeamView team(TeamDto dto) {
        McpTeamView view = new McpTeamView();
        view.setTeamId(dto.getTeamId());
        view.setWorkspaceId(dto.getWorkspaceId());
        view.setName(dto.getName());
        view.setUsername(dto.getUsername());
        view.setTag(dto.getTag());
        view.setTaskVisibility(name(dto.getTaskVisibility()));
        view.setTeamState(name(dto.getTeamState()));
        return view;
    }

    public McpWorkflowStatusView workflowStatus(TeamWorkflowStatusDto dto) {
        McpWorkflowStatusView view = new McpWorkflowStatusView();
        view.setWorkflowStatusId(dto.getTeamWorkflowStatusId());
        view.setTeamId(dto.getTeamId());
        view.setName(dto.getName());
        view.setStateGroup(name(dto.getWorkflowStateGroup()));
        view.setOrder(dto.getOrder());
        return view;
    }

    public McpTopicView topic(TopicDto dto) {
        McpTopicView view = new McpTopicView();
        view.setTopicId(dto.getTopicId());
        view.setTeamId(dto.getTeamId());
        view.setName(dto.getName());
        view.setTag(dto.getTag());
        view.setColor(dto.getColor());
        return view;
    }

    public McpTaskView task(TaskDto dto) {
        McpTaskView view = new McpTaskView();
        fillTask(view, dto);
        return view;
    }

    public McpTaskDetailView taskDetail(TaskDto dto) {
        McpTaskDetailView view = new McpTaskDetailView();
        fillTask(view, dto);
        view.setDescription(richText(dto.getDescription()));
        return view;
    }

    public McpBoardView board(TaskBoardDto dto) {
        McpBoardView view = new McpBoardView();
        view.setTaskBoardId(dto.getTaskBoardId());
        view.setWorkspaceId(dto.getWorkspaceId());
        view.setTeamId(dto.getTeamId());
        view.setTitle(dto.getTitle());
        view.setState(name(dto.getState()));
        view.setDueDate(instant(dto.getDueDate()));
        return view;
    }

    public McpNoteView note(NoteDto dto) {
        McpNoteView view = new McpNoteView();
        fillNote(view, dto);
        return view;
    }

    public McpNoteDetailView noteDetail(NoteDto dto) {
        McpNoteDetailView view = new McpNoteDetailView();
        fillNote(view, dto);
        view.setBody(richText(dto.getRichText()));
        return view;
    }

    public McpNotebookView notebook(NotebookDto dto) {
        McpNotebookView view = new McpNotebookView();
        view.setNotebookId(dto.getNotebookId());
        view.setWorkspaceId(dto.getWorkspaceId());
        view.setTitle(dto.getTitle());
        view.setDescription(dto.getDescription());
        view.setVisibility(name(dto.getVisibility()));
        return view;
    }

    public McpCalendarEventView calendarEvent(CalendarEventDto dto) {
        McpCalendarEventView view = new McpCalendarEventView();
        view.setCalendarEventId(dto.getCalendarEventId());
        view.setCalendarId(dto.getCalendarId());
        view.setWorkspaceId(dto.getWorkspaceId());
        view.setTitle(dto.getTitle());
        view.setStartsAt(instant(dto.getAssignedDate()));
        view.setEndsAt(instant(dto.getDueDate()));
        view.setLocation(dto.getLocation());
        view.setSourceType(name(dto.getCalendarEventSourceType()));
        view.setRelatedTaskId(Objects.isNull(dto.getRelatedTask()) ? null : dto.getRelatedTask().getTaskId());
        return view;
    }

    public McpFileView file(MaterialDto dto) {
        McpFileView view = new McpFileView();
        view.setMaterialId(dto.getMaterialId());
        view.setWorkspaceId(dto.getWorkspaceId());
        view.setName(dto.getName());
        view.setMaterialType(name(dto.getMaterialType()));
        view.setParentMaterialId(dto.getParentMaterialId());
        view.setMediaId(dto.getMediaId());
        view.setAccessType(name(dto.getMaterialAccessType()));
        return view;
    }

    public McpCommentView comment(CommentDto dto) {
        McpCommentView view = new McpCommentView();
        view.setCommentId(dto.getCommentId());
        view.setTaskId(dto.getTaskId());
        view.setAuthorAccountId(dto.getOwnerId());
        view.setAuthorUsername(Objects.isNull(dto.getOwner()) ? null : dto.getOwner().getUsername());
        view.setBody(richText(dto.getRichText()));
        view.setQuoteCommentId(Objects.isNull(dto.getQuote()) ? null : dto.getQuote().getCommentId());
        view.setCreatedAt(instant(dto.getCreatedDate()));
        return view;
    }

    private void fillWorkspace(McpWorkspaceView view, WorkspaceDto dto) {
        if (Objects.isNull(dto)) {
            return;
        }
        view.setWorkspaceId(dto.getWorkspaceId());
        view.setUsername(dto.getUsername());
        view.setTitle(dto.getTitle());
        view.setTier(name(dto.getTier()));
    }

    private void fillTask(McpTaskView view, TaskDto dto) {
        view.setTaskId(dto.getTaskId());
        view.setWorkspaceId(dto.getWorkspaceId());
        view.setTeamId(dto.getTeamId());
        view.setTitle(dto.getTitle());
        view.setReference(reference(dto));
        view.setWorkflowStatusId(dto.getWorkflowStatusId());
        if (Objects.nonNull(dto.getWorkflowStatus())) {
            view.setWorkflowStatusName(dto.getWorkflowStatus().getName());
            view.setWorkflowStateGroup(name(dto.getWorkflowStatus().getWorkflowStateGroup()));
        }
        view.setAssignedTo(dto.getAssignedTo());
        view.setOwnerId(dto.getOwnerId());
        view.setStartDate(instant(dto.getAssignedDate()));
        view.setDueDate(instant(dto.getDueDate()));
        view.setTopicId(dto.getTopicId());
    }

    private void fillNote(McpNoteView view, NoteDto dto) {
        view.setNoteId(dto.getNoteId());
        view.setNotebookId(dto.getNotebookId());
        view.setWorkspaceId(dto.getWorkspaceId());
        view.setTitle(dto.getTitle());
        view.setParentNoteId(dto.getParentNoteId());
        view.setOwnerId(dto.getOwnerId());
    }

    private String reference(TaskDto dto) {
        if (Objects.isNull(dto.getTeam()) || Objects.isNull(dto.getTeamTagNo())) {
            return null;
        }
        return dto.getTeam().getTag() + "-" + dto.getTeamTagNo();
    }

    private String richText(RichTextDto dto) {
        return Objects.isNull(dto) ? null : dto.getValue();
    }

    private String instant(ZonedDateTime value) {
        return Objects.isNull(value) ? null : DateTimeFormatter.ISO_INSTANT.format(value.toInstant());
    }

    private String instant(Date value) {
        return Objects.isNull(value) ? null : DateTimeFormatter.ISO_INSTANT.format(value.toInstant());
    }

    private String name(Enum<?> value) {
        return Objects.isNull(value) ? null : value.name();
    }
}
