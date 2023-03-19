package co.jinear.core.service.task;

import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceActivityType;
import co.jinear.core.model.vo.workspace.WorkspaceActivityCreateVo;
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

    public void initializeEditTitleActivity(String performedBy, TaskDto before, TaskDto after) {
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        vo.setOldState(before.getTitle());
        vo.setNewState(after.getTitle());
        vo.setType(WorkspaceActivityType.EDIT_TASK_TITLE);
        workspaceActivityService.createWorkspaceActivity(vo);
    }

    public void initializeEditDescriptionActivity(String performedBy, TaskDto before, TaskDto after) {
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before).map(TaskDto::getDescription).map(RichTextDto::getRichTextId).ifPresent(vo::setOldState);
        Optional.of(after).map(TaskDto::getDescription).map(RichTextDto::getRichTextId).ifPresent(vo::setNewState);
        vo.setType(WorkspaceActivityType.EDIT_TASK_DESC);
        workspaceActivityService.createWorkspaceActivity(vo);
    }

    public void initializeStatusUpdateActivity(String performedBy, TaskDto before, TaskDto after) {
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before).map(TaskDto::getWorkflowStatusId).ifPresent(vo::setOldState);
        Optional.of(after).map(TaskDto::getWorkflowStatusId).ifPresent(vo::setNewState);
        vo.setType(WorkspaceActivityType.TASK_UPDATE_WORKFLOW_STATUS);
        workspaceActivityService.createWorkspaceActivity(vo);
    }

    public void initializeTopicUpdateActivity(String performedBy, TaskDto before, TaskDto after) {
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before).map(TaskDto::getTopicId).ifPresent(vo::setOldState);
        Optional.of(after).map(TaskDto::getTopicId).ifPresent(vo::setNewState);
        vo.setType(WorkspaceActivityType.TASK_UPDATE_TOPIC);
        workspaceActivityService.createWorkspaceActivity(vo);
    }

    public void initializeDatesUpdateActivity(String performedBy, TaskDto before, TaskDto after) {
        initializeAssignedDateUpdateActivity(performedBy, before, after);
        initializeDueDateUpdateActivity(performedBy, before, after);
    }

    public void initializeAssigneeUpdateActivity(String performedBy, TaskDto before, TaskDto after) {
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before).map(TaskDto::getAssignedTo).ifPresent(vo::setOldState);
        Optional.of(after).map(TaskDto::getAssignedTo).ifPresent(vo::setNewState);
        vo.setType(WorkspaceActivityType.TASK_CHANGE_ASSIGNEE);
        workspaceActivityService.createWorkspaceActivity(vo);
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

    private void initializeAssignedDateUpdateActivity(String performedBy, TaskDto before, TaskDto after) {
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before)
                .map(TaskDto::getAssignedDate)
                .map(assignedDate -> Boolean.TRUE.equals(before.getHasPreciseAssignedDate()) ? ZonedDateHelper.formatWithDateTimeFormat5(assignedDate) : ZonedDateHelper.formatWithDateTimeFormat4(assignedDate))
                .ifPresent(vo::setOldState);
        Optional.of(after)
                .map(TaskDto::getAssignedDate)
                .map(assignedDate -> Boolean.TRUE.equals(after.getHasPreciseAssignedDate()) ? ZonedDateHelper.formatWithDateTimeFormat5(assignedDate) : ZonedDateHelper.formatWithDateTimeFormat4(assignedDate))
                .ifPresent(vo::setNewState);
        vo.setType(WorkspaceActivityType.TASK_CHANGE_ASSIGNED_DATE);
        if (!Objects.equals(vo.getOldState(), vo.getNewState())) {
            workspaceActivityService.createWorkspaceActivity(vo);
        }
    }

    private void initializeDueDateUpdateActivity(String performedBy, TaskDto before, TaskDto after) {
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before)
                .map(TaskDto::getDueDate)
                .map(dueDate -> Boolean.TRUE.equals(before.getHasPreciseDueDate()) ? ZonedDateHelper.formatWithDateTimeFormat5(dueDate) : ZonedDateHelper.formatWithDateTimeFormat4(dueDate))
                .ifPresent(vo::setOldState);
        Optional.of(after)
                .map(TaskDto::getDueDate)
                .map(dueDate -> Boolean.TRUE.equals(after.getHasPreciseDueDate()) ? ZonedDateHelper.formatWithDateTimeFormat5(dueDate) : ZonedDateHelper.formatWithDateTimeFormat4(dueDate))
                .ifPresent(vo::setNewState);
        vo.setType(WorkspaceActivityType.TASK_CHANGE_DUE_DATE);
        if (!Objects.equals(vo.getOldState(), vo.getNewState())) {
            workspaceActivityService.createWorkspaceActivity(vo);
        }
    }
}
