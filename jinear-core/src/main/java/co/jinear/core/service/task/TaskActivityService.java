package co.jinear.core.service.task;

import co.jinear.core.model.dto.richtext.RichTextDto;
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

    public void initializeEditTitleActivity(String performedBy, TaskDto before, TaskDto after) {
        WorkspaceActivityType type = WorkspaceActivityType.EDIT_TASK_TITLE;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        vo.setOldState(before.getTitle());
        vo.setNewState(after.getTitle());
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(after, type);
    }

    public void initializeEditDescriptionActivity(String performedBy, TaskDto before, TaskDto after) {
        WorkspaceActivityType type = WorkspaceActivityType.EDIT_TASK_DESC;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before).map(TaskDto::getDescription).map(RichTextDto::getRichTextId).ifPresent(vo::setOldState);
        Optional.of(after).map(TaskDto::getDescription).map(RichTextDto::getRichTextId).ifPresent(vo::setNewState);
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(after, type);
    }

    public void initializeStatusUpdateActivity(String performedBy, TaskDto before, TaskDto after) {
        WorkspaceActivityType type = WorkspaceActivityType.TASK_UPDATE_WORKFLOW_STATUS;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before).map(TaskDto::getWorkflowStatusId).ifPresent(vo::setOldState);
        Optional.of(after).map(TaskDto::getWorkflowStatusId).ifPresent(vo::setNewState);
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(after, type);
    }

    public void initializeTopicUpdateActivity(String performedBy, TaskDto before, TaskDto after) {
        WorkspaceActivityType type = WorkspaceActivityType.TASK_UPDATE_TOPIC;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before).map(TaskDto::getTopicId).ifPresent(vo::setOldState);
        Optional.of(after).map(TaskDto::getTopicId).ifPresent(vo::setNewState);
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(after, type);
    }

    public void initializeDatesUpdateActivity(String performedBy, TaskDto before, TaskDto after) {
        initializeAssignedDateUpdateActivity(performedBy, before, after);
        initializeDueDateUpdateActivity(performedBy, before, after);
    }

    public void initializeAssigneeUpdateActivity(String performedBy, TaskDto before, TaskDto after) {
        WorkspaceActivityType type = WorkspaceActivityType.TASK_CHANGE_ASSIGNEE;
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before).map(TaskDto::getAssignedTo).ifPresent(vo::setOldState);
        Optional.of(after).map(TaskDto::getAssignedTo).ifPresent(vo::setNewState);
        vo.setType(type);
        workspaceActivityService.createWorkspaceActivity(vo);
        notifyTaskSubscribers(after, type);
    }

    private void initializeAssignedDateUpdateActivity(String performedBy, TaskDto before, TaskDto after) {
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
            notifyTaskSubscribers(after, type);
        }
    }

    private void initializeDueDateUpdateActivity(String performedBy, TaskDto before, TaskDto after) {
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
            notifyTaskSubscribers(after, type);
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

    private void notifyTaskSubscribers(TaskDto after, WorkspaceActivityType type) {
        NotifyTaskSubscribersVo notifyTaskSubscribersVo = new NotifyTaskSubscribersVo();
        notifyTaskSubscribersVo.setTaskDto(after);
        notifyTaskSubscribersVo.setWorkspaceActivityType(type);
        taskReachOutService.notifyTaskSubscribers(notifyTaskSubscribersVo);
    }
}
