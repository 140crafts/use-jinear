package co.jinear.core.manager.task;

import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.enumtype.team.TeamMemberRoleType;
import co.jinear.core.model.enumtype.team.TeamTaskVisibilityType;
import co.jinear.core.model.request.task.TaskSearchRequest;
import co.jinear.core.model.response.task.TaskSearchResponse;
import co.jinear.core.model.vo.task.TaskFtsSearchVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskSearchService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskSearchManager {

    private final TaskSearchService taskSearchService;
    private final SessionInfoService sessionInfoService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamMemberRetrieveService teamMemberRetrieveService;

    public TaskSearchResponse searchTask(TaskSearchRequest taskSearchRequest, int page) {
        String currentAccountId = sessionInfoService.currentAccountId();
        List<TeamMemberDto> teamMemberDtos = validateAccess(currentAccountId, taskSearchRequest);
        log.info("Search task has begun. accountId: {}", currentAccountId);
        Page<TaskDto> taskDtoPage = searchTasks(taskSearchRequest, teamMemberDtos, currentAccountId, page);
        return mapResponse(taskDtoPage);
    }

    private Page<TaskDto> searchTasks(TaskSearchRequest taskSearchRequest, List<TeamMemberDto> teamMemberDtos, String currentAccountId, int page) {
        List<String> visibleToAllTeamIds = new ArrayList<>();
        List<String> ownerAndAssigneeTeamIds = new ArrayList<>();

        teamMemberDtos.forEach(teamMemberDto -> {
            TeamDto teamDto = teamMemberDto.getTeam();
            String teamId = teamDto.getTeamId();
            if (TeamMemberRoleType.ADMIN.equals(teamMemberDto.getRole()) || TeamTaskVisibilityType.VISIBLE_TO_ALL_TEAM_MEMBERS.equals(teamDto.getTaskVisibility())) {
                visibleToAllTeamIds.add(teamId);
            } else {
                ownerAndAssigneeTeamIds.add(teamId);
            }
        });

        TaskFtsSearchVo taskFtsSearchVo = mapVo(taskSearchRequest, visibleToAllTeamIds, ownerAndAssigneeTeamIds, currentAccountId, page);
        return taskSearchService.search(taskFtsSearchVo);
    }

    private List<TeamMemberDto> validateAccess(String currentAccountId, TaskSearchRequest taskSearchRequest) {
        workspaceValidator.validateHasAccess(currentAccountId, taskSearchRequest.getWorkspaceId());
        validateTeamIdListHasNoBlankElement(taskSearchRequest);
        return retrieveMemberships(taskSearchRequest, currentAccountId);
    }

    private void validateTeamIdListHasNoBlankElement(TaskSearchRequest taskSearchRequest) {
        Optional.of(taskSearchRequest)
                .map(TaskSearchRequest::getTeamIdList)
                .map(Collection::stream)
                .filter(teamIdStream -> teamIdStream.anyMatch(Strings::isBlank))
                .ifPresent(teamIdStream -> {
                    throw new NotValidException();
                });
    }

    private List<TeamMemberDto> retrieveMemberships(TaskSearchRequest taskSearchRequest, String currentAccount) {
        String workspaceId = taskSearchRequest.getWorkspaceId();
        return Optional.of(taskSearchRequest)
                .map(TaskSearchRequest::getTeamIdList)
                .filter(teamIds -> !teamIds.isEmpty())
                .map(teamIds -> teamMemberRetrieveService.retrieveMemberships(workspaceId, currentAccount, teamIds))
                .orElseGet(() -> teamMemberRetrieveService.retrieveAllTeamMembershipsOfAnAccount(currentAccount, workspaceId));

    }

    private TaskFtsSearchVo mapVo(TaskSearchRequest taskSearchRequest, List<String> visibleToAllTeamIds, List<String> ownerAndAssigneeTeamIds, String currentAccountId, int page) {
        TaskFtsSearchVo taskFtsSearchVo = new TaskFtsSearchVo();
        taskFtsSearchVo.setQuery(taskSearchRequest.getQuery());
        taskFtsSearchVo.setWorkspaceId(taskSearchRequest.getWorkspaceId());
        taskFtsSearchVo.setVisibleToAllTeamIds(visibleToAllTeamIds);
        taskFtsSearchVo.setOwnerOrAssigneeTeamIds(ownerAndAssigneeTeamIds);
        taskFtsSearchVo.setAssignedTo(currentAccountId);
        taskFtsSearchVo.setOwnerId(currentAccountId);
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
