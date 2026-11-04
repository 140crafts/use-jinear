package co.jinear.core.converter.mcp;

import co.jinear.core.model.dto.calendar.CalendarEventDto;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.dto.note.NoteContentDto;
import co.jinear.core.model.dto.note.NoteDto;
import co.jinear.core.model.dto.note.NoteEditResultDto;
import co.jinear.core.model.dto.notebook.NotebookDto;
import co.jinear.core.model.dto.notification.NotificationEventDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.reminder.ReminderDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.task.ChecklistDto;
import co.jinear.core.model.dto.task.ChecklistItemDto;
import co.jinear.core.model.dto.task.CommentDto;
import co.jinear.core.model.dto.task.PlainTaskBoardEntryDto;
import co.jinear.core.model.dto.task.RelatedTaskDto;
import co.jinear.core.model.dto.task.TaskCollaboratorDto;
import co.jinear.core.model.dto.task.TaskRelationDto;
import co.jinear.core.model.dto.task.TaskBoardDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskReminderDto;
import co.jinear.core.model.dto.task.TaskReminderScheduleDto;
import co.jinear.core.model.enumtype.task.TaskReminderType;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.dto.topic.TopicDto;
import co.jinear.core.model.dto.workspace.DetailedWorkspaceMemberDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.dto.workspace.WorkspaceMemberDto;
import co.jinear.core.model.mcp.view.McpBoardView;
import co.jinear.core.model.mcp.view.McpCalendarEventView;
import co.jinear.core.model.mcp.view.McpChecklistItemView;
import co.jinear.core.model.mcp.view.McpChecklistView;
import co.jinear.core.model.mcp.view.McpCommentView;
import co.jinear.core.model.mcp.view.McpFileView;
import co.jinear.core.model.mcp.view.McpMemberView;
import co.jinear.core.model.mcp.view.McpNoteDetailView;
import co.jinear.core.model.mcp.view.McpNoteView;
import co.jinear.core.model.mcp.view.McpNotificationView;
import co.jinear.core.model.mcp.view.McpNotebookView;
import co.jinear.core.model.mcp.view.McpTaskAttachmentView;
import co.jinear.core.model.mcp.view.McpTaskBoardEntryView;
import co.jinear.core.model.mcp.view.McpTaskCollaboratorView;
import co.jinear.core.model.mcp.view.McpTaskRelationView;
import co.jinear.core.model.mcp.view.McpTaskReminderView;
import co.jinear.core.model.mcp.view.McpNoteEditView;
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
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

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
        view.setCollaborators(mapAll(dto.getTaskCollaborators(), this::taskCollaborator));
        view.setRelations(mapAll(dto.getRelations(), this::taskRelation));
        view.setRelatedIn(mapAll(dto.getRelatedIn(), this::taskRelation));
        view.setChecklists(mapAll(dto.getChecklists(), this::checklist));
        view.setBoards(mapAll(dto.getTaskBoardEntries(), this::taskBoardEntry));
        return view;
    }

    public McpTaskRelationView taskRelation(TaskRelationDto dto) {
        McpTaskRelationView view = new McpTaskRelationView();
        view.setTaskRelationId(dto.getTaskRelationId());
        view.setRelationType(name(dto.getRelationType()));
        view.setTaskId(dto.getTaskId());
        view.setTaskTitle(relatedTaskTitle(dto.getTask()));
        view.setRelatedTaskId(dto.getRelatedTaskId());
        view.setRelatedTaskTitle(relatedTaskTitle(dto.getRelatedTask()));
        return view;
    }

    public McpChecklistView checklist(ChecklistDto dto) {
        List<ChecklistItemDto> items = Optional.ofNullable(dto.getChecklistItems())
                .map(List::copyOf)
                .orElseGet(List::of);
        McpChecklistView view = new McpChecklistView();
        view.setChecklistId(dto.getChecklistId());
        view.setTaskId(dto.getTaskId());
        view.setTitle(dto.getTitle());
        view.setItems(items.stream()
                .sorted(Comparator.comparing(ChecklistItemDto::getCreatedDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(this::checklistItem)
                .toList());
        return view;
    }

    public McpChecklistItemView checklistItem(ChecklistItemDto dto) {
        McpChecklistItemView view = new McpChecklistItemView();
        view.setChecklistItemId(dto.getChecklistItemId());
        view.setChecklistId(dto.getChecklistId());
        view.setLabel(dto.getLabel());
        view.setChecked(dto.getIsChecked());
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

    public McpNoteDetailView noteDetail(NoteDto dto, NoteContentDto content) {
        McpNoteDetailView view = new McpNoteDetailView();
        fillNote(view, dto);
        view.setBody(content.getHtml());
        return view;
    }

    public McpNoteEditView noteEdit(NoteEditResultDto dto) {
        McpNoteEditView view = new McpNoteEditView();
        view.setNoteId(dto.getNoteId());
        view.setUrl(dto.getUrl());
        view.setChanged(dto.isChanged());
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

    public McpTaskAttachmentView taskAttachment(MediaDto dto, String downloadUrl) {
        McpTaskAttachmentView view = new McpTaskAttachmentView();
        view.setMediaId(dto.getMediaId());
        view.setTaskId(dto.getRelatedObjectId());
        view.setName(dto.getOriginalName());
        view.setContentType(dto.getContentType());
        view.setSize(dto.getSize());
        view.setVisibility(name(dto.getVisibility()));
        view.setCreatedAt(instant(dto.getCreatedDate()));
        view.setDownloadUrl(downloadUrl);
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

    public McpTaskReminderView taskReminder(TaskReminderScheduleDto dto) {
        TaskReminderDto taskReminder = dto.getTaskReminder();
        ReminderDto reminder = taskReminder.getReminder();
        McpTaskReminderView view = new McpTaskReminderView();
        view.setTaskReminderId(taskReminder.getTaskReminderId());
        view.setTaskId(taskReminder.getTaskId());
        view.setType(taskReminderType(taskReminder.getTaskReminderType()));
        view.setNextRemindAt(instant(dto.getNextRemindDate()));
        if (Objects.nonNull(reminder)) {
            view.setRepeat(name(reminder.getRepeatType()));
            view.setRepeatUntil(instant(reminder.getRepeatEnd()));
            view.setCreatedByAccountId(reminder.getOwnerId());
        }
        return view;
    }

    private String taskReminderType(TaskReminderType taskReminderType) {
        if (TaskReminderType.ASSIGNED_DATE.equals(taskReminderType)) {
            return "START_DATE";
        }
        return name(taskReminderType);
    }

    public McpNotificationView notification(NotificationEventDto dto) {
        McpNotificationView view = new McpNotificationView();
        view.setNotificationId(dto.getNotificationEventId());
        view.setType(name(dto.getNotificationType()));
        view.setTitle(dto.getTitle());
        view.setText(dto.getText());
        view.setUrl(dto.getLaunchUrl());
        view.setRead(dto.getIsRead());
        view.setCreatedAt(instant(dto.getCreatedDate()));
        return view;
    }

    private McpTaskCollaboratorView taskCollaborator(TaskCollaboratorDto dto) {
        McpTaskCollaboratorView view = new McpTaskCollaboratorView();
        view.setAccountId(dto.getAccountId());
        view.setUsername(Optional.ofNullable(dto.getCollaborator()).map(PlainAccountProfileDto::getUsername).orElse(null));
        return view;
    }

    private McpTaskBoardEntryView taskBoardEntry(PlainTaskBoardEntryDto dto) {
        McpTaskBoardEntryView view = new McpTaskBoardEntryView();
        view.setTaskBoardEntryId(dto.getTaskBoardEntryId());
        view.setTaskBoardId(dto.getTaskBoardId());
        view.setTitle(Optional.ofNullable(dto.getTaskBoard()).map(TaskBoardDto::getTitle).orElse(null));
        view.setOrder(dto.getOrder());
        return view;
    }

    private String relatedTaskTitle(RelatedTaskDto dto) {
        return Optional.ofNullable(dto).map(RelatedTaskDto::getTitle).orElse(null);
    }

    private <S, T> List<T> mapAll(Collection<S> source, Function<S, T> mapper) {
        return Optional.ofNullable(source)
                .map(values -> values.stream().map(mapper).toList())
                .orElseGet(List::of);
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
        view.setUrl(dto.getUrl());
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
        view.setUrl(dto.getUrl());
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
