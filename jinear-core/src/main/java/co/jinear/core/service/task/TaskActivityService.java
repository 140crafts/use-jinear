package co.jinear.core.service.task;

import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.ChecklistDto;
import co.jinear.core.model.dto.task.TaskBoardEntryDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceActivityType;
import co.jinear.core.model.vo.task.NotifyTaskSubscribersVo;
import co.jinear.core.model.vo.workspace.WorkspaceActivityCreateVo;
import co.jinear.core.service.task.taskreachout.TaskReachOutService;
import co.jinear.core.service.workspace.activity.WorkspaceActivityService;
import co.jinear.core.system.util.ZonedDateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskActivityService {

    private final WorkspaceActivityService workspaceActivityService;
    private final TaskReachOutService taskReachOutService;

    public void initializeNewTaskActivity(String performedBy, String performingAccountSessionId, TaskDto taskDto) {
        WorkspaceActivityType type = WorkspaceActivityType.TASK_INITIALIZED;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, taskDto);
        vo.setNewState(taskDto.getTitle());
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(taskDto, type, performingAccountSessionId);
    }

    public void initializeEditTitleActivity(String performedBy, String performingAccountSessionId, TaskDto before, TaskDto after) {
        WorkspaceActivityType type = WorkspaceActivityType.EDIT_TASK_TITLE;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        vo.setOldState(before.getTitle());
        vo.setNewState(after.getTitle());
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(after, type, performingAccountSessionId);
    }

    public void initializeEditDescriptionActivity(String performedBy, String performingAccountSessionId, TaskDto before, TaskDto after) {
        WorkspaceActivityType type = WorkspaceActivityType.EDIT_TASK_DESC;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before).map(TaskDto::getDescription).map(RichTextDto::getRichTextId).ifPresent(vo::setOldState);
        Optional.of(after).map(TaskDto::getDescription).map(RichTextDto::getRichTextId).ifPresent(vo::setNewState);
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(after, type, performingAccountSessionId);
    }

    public void initializeStatusUpdateActivity(String performedBy, String performingAccountSessionId, TaskDto before, TaskDto after) {
        WorkspaceActivityType type = WorkspaceActivityType.TASK_UPDATE_WORKFLOW_STATUS;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before).map(TaskDto::getWorkflowStatusId).ifPresent(vo::setOldState);
        Optional.of(after).map(TaskDto::getWorkflowStatusId).ifPresent(vo::setNewState);
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(after, type, performingAccountSessionId);
    }

    public void initializeTopicUpdateActivity(String performedBy, String performingAccountSessionId, TaskDto before, TaskDto after) {
        WorkspaceActivityType type = WorkspaceActivityType.TASK_UPDATE_TOPIC;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before).map(TaskDto::getTopicId).ifPresent(vo::setOldState);
        Optional.of(after).map(TaskDto::getTopicId).ifPresent(vo::setNewState);
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(after, type, performingAccountSessionId);
    }

    public void initializeDatesUpdateActivity(String performedBy, String performingAccountSessionId, TaskDto before, TaskDto after) {
        initializeAssignedDateUpdateActivity(performedBy, performingAccountSessionId, before, after);
        initializeDueDateUpdateActivity(performedBy, performingAccountSessionId, before, after);
    }

    public void initializeAssigneeUpdateActivity(String performedBy, String performingAccountSessionId, TaskDto before, TaskDto after) {
        WorkspaceActivityType type = WorkspaceActivityType.TASK_CHANGE_ASSIGNEE;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before).map(TaskDto::getAssignedTo).ifPresent(vo::setOldState);
        Optional.of(after).map(TaskDto::getAssignedTo).ifPresent(vo::setNewState);
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(after, type, performingAccountSessionId);
    }

    public void initializeChecklistCreateActivity(String performedBy, String performingAccountSessionId, TaskDto after, ChecklistDto checklistDto) {
        WorkspaceActivityType type = WorkspaceActivityType.CHECKLIST_INITIALIZED;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after, checklistDto.getChecklistId());
        vo.setNewState(checklistDto.getChecklistId());
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(after, type, performingAccountSessionId);
    }

    public void initializeChecklistRemovedActivity(String performedBy, String performingAccountSessionId, TaskDto after, ChecklistDto checklistDto) {
        WorkspaceActivityType type = WorkspaceActivityType.CHECKLIST_REMOVED;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after, checklistDto.getChecklistId());
        vo.setOldState(checklistDto.getChecklistId());
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(after, type, performingAccountSessionId);
    }

    public void initializeChecklistTitleChangedActivity(String performedBy, String performingAccountSessionId, TaskDto after, ChecklistDto oldChecklist, ChecklistDto newChecklist) {
        WorkspaceActivityType type = WorkspaceActivityType.CHECKLIST_TITLE_CHANGED;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after, newChecklist.getChecklistId());
        vo.setOldState(oldChecklist.getTitle());
        vo.setNewState(newChecklist.getTitle());
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(after, type, performingAccountSessionId);
    }

    public void initializeChecklistItemCheckedStatusChangedActivity(String performedBy, String performingAccountSessionId, TaskDto after, String checklistItemId, Boolean oldCheckedStatus, Boolean newCheckedStatus) {
        WorkspaceActivityType type = WorkspaceActivityType.CHECKLIST_ITEM_CHECKED_STATUS_CHANGED;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after, checklistItemId);
        Optional.ofNullable(oldCheckedStatus).map(String::valueOf).ifPresent(vo::setOldState);
        Optional.ofNullable(newCheckedStatus).map(String::valueOf).ifPresent(vo::setNewState);
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(after, type, performingAccountSessionId);
    }

    public void initializeChecklistItemLabelChangedActivity(String performedBy, String performingAccountSessionId, TaskDto after, String checklistItemId, String oldLabel, String newLabel) {
        WorkspaceActivityType type = WorkspaceActivityType.CHECKLIST_ITEM_LABEL_CHANGED;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after, checklistItemId);
        Optional.ofNullable(oldLabel).ifPresent(vo::setOldState);
        Optional.ofNullable(newLabel).ifPresent(vo::setNewState);
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(after, type, performingAccountSessionId);
    }

    public void initializeChecklistItemRemovedActivity(String performedBy, String performingAccountSessionId, TaskDto after, String checklistItemId) {
        WorkspaceActivityType type = WorkspaceActivityType.CHECKLIST_ITEM_REMOVED;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after, checklistItemId);
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(after, type, performingAccountSessionId);
    }

    public void initializeChecklistItemInitializedActivity(String performedBy, String performingAccountSessionId, TaskDto after, String checklistItemId) {
        WorkspaceActivityType type = WorkspaceActivityType.CHECKLIST_ITEM_INITIALIZED;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after, checklistItemId);
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(after, type, performingAccountSessionId);
    }

    public void initializeTaskAddedToTaskBoardActivity(String performedBy, String performingAccountSessionId, TaskDto taskDto, String taskBoardId) {
        WorkspaceActivityType type = WorkspaceActivityType.TASK_BOARD_ENTRY_INIT;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, taskDto, taskBoardId);
        vo.setType(type);
        vo.setBoardId(taskBoardId);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(taskDto, type, performingAccountSessionId);
    }

    public void initializeTaskRemovedFromTaskBoardActivity(String performedBy, String performingAccountSessionId, TaskBoardEntryDto boardEntryDto) {
        WorkspaceActivityType type = WorkspaceActivityType.TASK_BOARD_ENTRY_REMOVED;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, boardEntryDto.getTask(), boardEntryDto.getTaskBoardId());
        vo.setType(type);
        vo.setBoardId(boardEntryDto.getTaskBoardId());
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(boardEntryDto.getTask(), type, performingAccountSessionId);
    }

    public void initializeTaskOrderChangedOnTaskBoardActivity(String performedBy, String performingAccountSessionId, TaskBoardEntryDto boardEntryDto) {
        WorkspaceActivityType type = WorkspaceActivityType.TASK_BOARD_ENTRY_ORDER_CHANGE;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, boardEntryDto.getTask(), boardEntryDto.getTaskBoardId());
        vo.setType(type);
        vo.setBoardId(boardEntryDto.getTaskBoardId());
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(boardEntryDto.getTask(), type, performingAccountSessionId);
    }

    public void initializeTaskAttachmentAddedActivity(String performedBy, String performingAccountSessionId, TaskDto taskDto, AccessibleMediaDto accessibleMediaDto) {
        WorkspaceActivityType type = WorkspaceActivityType.ATTACHMENT_ADDED;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, taskDto, accessibleMediaDto.getMediaId());
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(taskDto, type, performingAccountSessionId);
    }

    public void initializeTaskAttachmentDeletedActivity(String performedBy, String performingAccountSessionId, TaskDto taskDto, AccessibleMediaDto accessibleMediaDto) {
        WorkspaceActivityType type = WorkspaceActivityType.ATTACHMENT_DELETED;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, taskDto, accessibleMediaDto.getMediaId());
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(taskDto, type, performingAccountSessionId);
    }

    public void initializeNewCommentActivity(String performedBy, String performingAccountSessionId,String commentId, TaskDto before) {
        WorkspaceActivityType type = WorkspaceActivityType.TASK_NEW_COMMENT;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, before);
        vo.setNewState(commentId);
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(before, type, performingAccountSessionId);
    }

    private void initializeAssignedDateUpdateActivity(String performedBy, String performingAccountSessionId, TaskDto before, TaskDto after) {
        WorkspaceActivityType type = WorkspaceActivityType.TASK_CHANGE_ASSIGNED_DATE;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before)
                .map(TaskDto::getAssignedDate)
                .map(assignedDate -> Boolean.TRUE.equals(before.getHasPreciseAssignedDate()) ? ZonedDateHelper.formatWithDateTimeFormat5(assignedDate) : ZonedDateHelper.formatWithDateTimeFormat4(assignedDate))
                .ifPresent(vo::setOldState);
        Optional.of(after)
                .map(TaskDto::getAssignedDate)
                .map(assignedDate -> Boolean.TRUE.equals(after.getHasPreciseAssignedDate()) ? ZonedDateHelper.formatWithDateTimeFormat5(assignedDate) : ZonedDateHelper.formatWithDateTimeFormat4(assignedDate))
                .ifPresent(vo::setNewState);
        vo.setType(type);
        if (!Objects.equals(vo.getOldState(), vo.getNewState())) {
            workspaceActivityService.createWorkspaceActivity(vo);
            notifyTaskSubscribers(after, type, performingAccountSessionId);
        }
    }

    private void initializeDueDateUpdateActivity(String performedBy, String performingAccountSessionId, TaskDto before, TaskDto after) {
        WorkspaceActivityType type = WorkspaceActivityType.TASK_CHANGE_DUE_DATE;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before)
                .map(TaskDto::getDueDate)
                .map(dueDate -> Boolean.TRUE.equals(before.getHasPreciseDueDate()) ? ZonedDateHelper.formatWithDateTimeFormat5(dueDate) : ZonedDateHelper.formatWithDateTimeFormat4(dueDate))
                .ifPresent(vo::setOldState);
        Optional.of(after)
                .map(TaskDto::getDueDate)
                .map(dueDate -> Boolean.TRUE.equals(after.getHasPreciseDueDate()) ? ZonedDateHelper.formatWithDateTimeFormat5(dueDate) : ZonedDateHelper.formatWithDateTimeFormat4(dueDate))
                .ifPresent(vo::setNewState);
        vo.setType(type);
        if (!Objects.equals(vo.getOldState(), vo.getNewState())) {
            workspaceActivityService.createWorkspaceActivity(vo);
            notifyTaskSubscribers(after, type, performingAccountSessionId);
        }
    }

    private WorkspaceActivityCreateVo buildWithCommonValues(String performedBy, TaskDto taskDto) {
        return WorkspaceActivityCreateVo
                .builder()
                .workspaceId(taskDto.getWorkspaceId())
                .teamId(taskDto.getTeamId())
                .taskId(taskDto.getTaskId())
                .performedBy(performedBy)
                .relatedObjectId(taskDto.getTaskId())
                .build();
    }

    private WorkspaceActivityCreateVo buildWithCommonValues(String performedBy, TaskDto taskDto, String relatedObjectId) {
        return WorkspaceActivityCreateVo
                .builder()
                .workspaceId(taskDto.getWorkspaceId())
                .teamId(taskDto.getTeamId())
                .taskId(taskDto.getTaskId())
                .performedBy(performedBy)
                .relatedObjectId(relatedObjectId)
                .build();
    }

    private void notifyTaskSubscribers(TaskDto taskDto, WorkspaceActivityType type, String performingAccountSessionId) {
        NotifyTaskSubscribersVo notifyTaskSubscribersVo = new NotifyTaskSubscribersVo();
        notifyTaskSubscribersVo.setTaskDto(taskDto);
        notifyTaskSubscribersVo.setWorkspaceActivityType(type);
        notifyTaskSubscribersVo.setPerformingAccountSessionId(performingAccountSessionId);
        taskReachOutService.notifyTaskSubscribers(notifyTaskSubscribersVo);
    }
}
