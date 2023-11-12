package co.jinear.core.manager.task;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.TaskSearchResultDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.response.task.TaskSearchResponse;
import co.jinear.core.model.vo.task.TaskSearchVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskSearchService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static co.jinear.core.model.enumtype.team.TeamMemberRoleType.ADMIN;
import static co.jinear.core.model.enumtype.team.TeamTaskVisibilityType.OWNER_ASSIGNEE_AND_ADMINS;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskSearchManager {

    private final TaskSearchService taskSearchService;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamMemberRetrieveService teamMemberRetrieveService;

    public TaskSearchResponse searchTask(String title, String workspaceId, String teamId, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TeamMemberDto teamMemberDto = validateAccess(currentAccountId, workspaceId, teamId);
        log.info("Search task has begun. accountId: {}", currentAccountId);
        TaskSearchVo taskSearchVo = mapVo(title, workspaceId, teamMemberDto, page);
        Page<TaskSearchResultDto> taskDtoPage = searchTasks(teamMemberDto, taskSearchVo);
        return mapResponse(taskDtoPage);
    }

    private Page<TaskSearchResultDto> searchTasks(TeamMemberDto teamMemberDto, TaskSearchVo taskSearchVo) {
        return Optional.of(teamMemberDto)
                .map(TeamMemberDto::getTeam)
                .map(TeamDto::getTaskVisibility)
                .filter(teamTaskVisibilityType -> OWNER_ASSIGNEE_AND_ADMINS.equals(teamTaskVisibilityType) && !ADMIN.equals(teamMemberDto.getRole()))
                .map(v -> taskSearchService.searchTasksWithAssigneeOrOwner(taskSearchVo))
                .orElseGet(() -> taskSearchService.searchTasks(taskSearchVo));
    }

    private TeamMemberDto validateAccess(String currentAccountId, String workspaceId, String teamId) {
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        return teamMemberRetrieveService.retrieveMembership(workspaceId, teamId, currentAccountId)
                .orElseThrow(NoAccessException::new);
    }

    private TaskSearchVo mapVo(String title, String workspaceId, TeamMemberDto teamMemberDto, int page) {
        TaskSearchVo taskSearchVo = new TaskSearchVo();
        taskSearchVo.setTitle(title);
        taskSearchVo.setWorkspaceId(workspaceId);
        taskSearchVo.setTeamId(teamMemberDto.getTeamId());
        taskSearchVo.setAssignedTo(teamMemberDto.getAccountId());
        taskSearchVo.setOwnerId(teamMemberDto.getAccountId());
        taskSearchVo.setPage(page);
        return taskSearchVo;
    }

    private TaskSearchResponse mapResponse(Page<TaskSearchResultDto> taskDtoPage) {
        PageDto<TaskSearchResultDto> data = new PageDto<>(taskDtoPage);
        TaskSearchResponse response = new TaskSearchResponse();
        response.setResult(data);
        return response;
    }
}
