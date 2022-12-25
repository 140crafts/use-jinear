package co.jinear.core.service.task;

import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.workspace.WorkspaceActivityType;
import co.jinear.core.model.vo.workspace.WorkspaceActivityCreateVo;
import co.jinear.core.service.workspace.WorkspaceActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskActivityService {

    private final WorkspaceActivityService workspaceActivityService;

    @Async
    public void initializeEditTitleActivity(String performedBy, TaskDto before, TaskDto after) {
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        vo.setOldState(before.getTitle());
        vo.setNewState(after.getTitle());
        vo.setType(WorkspaceActivityType.EDIT_TASK_TITLE);
        workspaceActivityService.createWorkspaceActivity(vo);
    }

    @Async
    public void initializeEditDescriptionActivity(String performedBy, TaskDto before, TaskDto after) {
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before).map(TaskDto::getDescription).map(RichTextDto::getRichTextId).ifPresent(vo::setOldState);
        Optional.of(after).map(TaskDto::getDescription).map(RichTextDto::getRichTextId).ifPresent(vo::setNewState);
        vo.setType(WorkspaceActivityType.EDIT_TASK_DESC);
        workspaceActivityService.createWorkspaceActivity(vo);
    }

    @Async
    public void initializeStatusUpdateActivity(String performedBy, TaskDto before, TaskDto after) {
        WorkspaceActivityCreateVo vo = buildWithCommonValues(performedBy, after);
        Optional.of(before).map(TaskDto::getWorkflowStatusId).ifPresent(vo::setOldState);
        Optional.of(after).map(TaskDto::getWorkflowStatusId).ifPresent(vo::setNewState);
        vo.setType(WorkspaceActivityType.TASK_UPDATE_WORKFLOW_STATUS);
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
}
