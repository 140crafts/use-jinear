package co.jinear.core.manager.task;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.response.task.TaskSearchResponse;
import co.jinear.core.model.vo.task.TaskFtsSearchVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskSearchService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public TaskSearchResponse searchTask(String query, String workspaceId, String teamId, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        TeamMemberDto teamMemberDto = validateAccess(currentAccountId, workspaceId, teamId);
        log.info("Search task has begun. accountId: {}", currentAccountId);
        TaskFtsSearchVo taskFtsSearchVo = mapVo(query, workspaceId, teamMemberDto, page);
        Page<TaskDto> taskDtoPage = searchTasks(teamMemberDto, taskFtsSearchVo);
        return mapResponse(taskDtoPage);
    }

    private Page<TaskDto> searchTasks(TeamMemberDto teamMemberDto, TaskFtsSearchVo taskFtsSearchVo) {
        return Optional.of(teamMemberDto)
                .map(TeamMemberDto::getTeam)
                .map(TeamDto::getTaskVisibility)
                .filter(teamTaskVisibilityType -> OWNER_ASSIGNEE_AND_ADMINS.equals(teamTaskVisibilityType) && !ADMIN.equals(teamMemberDto.getRole()))
                .map(v -> taskSearchService.searchWithAssigneeOrOwner(taskFtsSearchVo))
                .orElseGet(() -> taskSearchService.search(taskFtsSearchVo));
    }

    private TeamMemberDto validateAccess(String currentAccountId, String workspaceId, String teamId) {
        workspaceValidator.validateHasAccess(currentAccountId, workspaceId);
        return teamMemberRetrieveService.retrieveMembership(workspaceId, teamId, currentAccountId)
                .orElseThrow(NoAccessException::new);
    }

    private TaskFtsSearchVo mapVo(String query, String workspaceId, TeamMemberDto teamMemberDto, int page) {
        TaskFtsSearchVo taskFtsSearchVo = new TaskFtsSearchVo();
        taskFtsSearchVo.setQuery(query);
        taskFtsSearchVo.setWorkspaceId(workspaceId);
        taskFtsSearchVo.setTeamIds(List.of(teamMemberDto.getTeamId()));
        taskFtsSearchVo.setAssignedTo(teamMemberDto.getAccountId());
        taskFtsSearchVo.setOwnerId(teamMemberDto.getAccountId());
        taskFtsSearchVo.setPage(page);
        return taskFtsSearchVo;
    }

    private TaskSearchResponse mapResponse(Page<TaskDto> taskDtoPage) {
        PageDto<TaskDto> data = new PageDto<>(taskDtoPage);
        TaskSearchResponse response = new TaskSearchResponse();
        response.setResult(data);
        return response;
    }
}
