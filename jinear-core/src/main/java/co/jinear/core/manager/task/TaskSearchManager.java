package co.jinear.core.manager.task;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.entity.task.TaskSearchVo;
import co.jinear.core.model.response.task.TaskSearchResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskSearchService;
import co.jinear.core.validator.team.TeamAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskSearchManager {

    private final TaskSearchService taskSearchService;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamAccessValidator teamAccessValidator;

    public TaskSearchResponse searchTask(String title, String workspaceId, String teamId, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateAccess(currentAccountId, workspaceId, teamId);
        log.info("Search task has begun. accountId: {}", currentAccountId);
        TaskSearchVo taskSearchVo = mapVo(title, workspaceId, teamId, page);
        Page<TaskDto> taskDtoPage = taskSearchService.searchTasks(taskSearchVo);
        return mapResponse(taskDtoPage);
    }

    private void validateAccess(String currentAccountId, String workspaceId, String teamId) {
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        teamAccessValidator.validateTeamAccess(currentAccountId, teamId);
    }

    private TaskSearchVo mapVo(String title, String workspaceId, String teamId, int page) {
        TaskSearchVo taskSearchVo = new TaskSearchVo();
        taskSearchVo.setTitle(title);
        taskSearchVo.setWorkspaceId(workspaceId);
        taskSearchVo.setTeamId(teamId);
        taskSearchVo.setPage(page);
        return taskSearchVo;
    }

    private TaskSearchResponse mapResponse(Page<TaskDto> taskDtoPage) {
        PageDto<TaskDto> data = new PageDto<>(taskDtoPage);
        TaskSearchResponse response = new TaskSearchResponse();
        response.setTaskDtoPage(data);
        return response;
    }
}
