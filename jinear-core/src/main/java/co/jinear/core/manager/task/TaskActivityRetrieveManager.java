package co.jinear.core.manager.task;

import co.jinear.core.model.dto.workspace.WorkspaceActivityDto;
import co.jinear.core.model.response.task.TaskActivityRetrieveResponse;
import co.jinear.core.model.vo.workspace.RetrieveTaskActivityVo;
import co.jinear.core.service.workspace.activity.WorkspaceActivityRetrieveService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskActivityRetrieveManager {

    private final WorkspaceActivityRetrieveService workspaceActivityRetrieveService;

    public TaskActivityRetrieveResponse retrieveTaskActivity(String taskId) {
        RetrieveTaskActivityVo retrieveTaskActivityVo = new RetrieveTaskActivityVo();
        retrieveTaskActivityVo.setTaskId(taskId);
        List<WorkspaceActivityDto> workspaceActivityDtos = workspaceActivityRetrieveService.retrieveTaskActivity(retrieveTaskActivityVo);
        return mapResponse(workspaceActivityDtos);
    }

    @NonNull
    private TaskActivityRetrieveResponse mapResponse(List<WorkspaceActivityDto> workspaceActivityDtos) {
        TaskActivityRetrieveResponse taskActivityRetrieveResponse = new TaskActivityRetrieveResponse();
        taskActivityRetrieveResponse.setTaskActivities(workspaceActivityDtos);
        return taskActivityRetrieveResponse;
    }
}
