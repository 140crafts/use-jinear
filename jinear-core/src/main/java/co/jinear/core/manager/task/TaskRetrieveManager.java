package co.jinear.core.manager.task;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.response.task.TaskResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.TaskRetrieveService;
import co.jinear.core.service.task.TaskTeamVisibilityMaskService;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.service.team.member.TeamMemberRetrieveService;
import co.jinear.core.service.workspace.WorkspaceRetrieveService;
import co.jinear.core.validator.task.TaskTeamVisibilityTypeAccessValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskRetrieveManager {

    private final WorkspaceRetrieveService workspaceRetrieveService;
    private final TeamRetrieveService teamRetrieveService;
    private final WorkspaceValidator workspaceValidator;
    private final TaskRetrieveService taskRetrieveService;
    private final SessionInfoService sessionInfoService;
    private final TeamMemberRetrieveService teamMemberRetrieveService;
    private final TaskTeamVisibilityTypeAccessValidator teamVisibilityTypeAccessValidator;
    private final TaskTeamVisibilityMaskService taskTeamVisibilityMaskService;

    public TaskResponse retrieveWithWorkspaceNameAndTeamTagNo(String workspaceName,
                                                              String teamTag,
                                                              Integer tagNo) {
        String currentAccountId = sessionInfoService.currentAccountId();
        log.info("Retrieve task with workspace name and team tag no has started. workspaceName: {}, teamTag: {}, tagNo: {}, currentAccountId:{}", workspaceName, teamTag, tagNo, currentAccountId);
        WorkspaceDto workspaceDto = workspaceRetrieveService.retrieveWorkspaceWithUsername(workspaceName);
        TeamDto teamDto = teamRetrieveService.retrieveTeamByTag(teamTag, workspaceDto.getWorkspaceId());
        TeamMemberDto teamMemberDto = validateAccess(currentAccountId, workspaceDto, teamDto);
        TaskDto taskDto = taskRetrieveService.retrieve(workspaceDto.getWorkspaceId(), teamDto.getTeamId(), tagNo);
        teamVisibilityTypeAccessValidator.validateTaskAccess(currentAccountId, teamMemberDto, taskDto.getOwnerId(), taskDto.getAssignedTo());
        taskDto = taskTeamVisibilityMaskService.maskRelations(currentAccountId, teamMemberDto, taskDto);
        return mapResponse(taskDto);
    }

    private TeamMemberDto validateAccess(String currentAccountId, WorkspaceDto workspaceDto, TeamDto teamDto) {
        workspaceValidator.validateHasAccess(currentAccountId, workspaceDto);
        return teamMemberRetrieveService.retrieveMembership(workspaceDto.getWorkspaceId(), teamDto.getTeamId(), currentAccountId)
                .orElseThrow(NoAccessException::new);
    }

    private TaskResponse mapResponse(TaskDto taskDto) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setTaskDto(taskDto);
        return taskResponse;
    }
}
