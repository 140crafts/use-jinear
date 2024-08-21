package co.jinear.core.manager.project;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.project.ProjectDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.project.ProjectRetrieveService;
import co.jinear.core.service.project.ProjectTeamOperationService;
import co.jinear.core.service.task.TaskListingService;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.validator.project.ProjectAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static co.jinear.core.model.enumtype.team.TeamStateType.ARCHIVED;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectTeamManager {

    private final SessionInfoService sessionInfoService;
    private final ProjectAccessValidator projectAccessValidator;
    private final ProjectRetrieveService projectRetrieveService;
    private final TeamRetrieveService teamRetrieveService;
    private final ProjectTeamOperationService projectTeamOperationService;
    private final PassiveService passiveService;
    private final TaskListingService taskListingService;

    public BaseResponse assignToTeam(String projectId, String teamId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        projectAccessValidator.validateHasExplicitAccess(projectId, currentAccountId);
        log.info("Assign to team has started. currentAccountId: {}", currentAccountId);
        validateProjectAndTeamWithinSameWorkspace(projectId, teamId);
        projectTeamOperationService.initialize(projectId, teamId);
        return new BaseResponse();
    }

    public BaseResponse removeTeamFromProject(String projectId, String teamId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        projectAccessValidator.validateHasExplicitAccess(projectId, currentAccountId);
        log.info("Remove team from project has started. currentAccountId: {}", currentAccountId);
        String passiveId = projectTeamOperationService.remove(projectId, teamId);
        passiveService.assignOwnership(passiveId, currentAccountId);
        return new BaseResponse();
    }

    private void validateProjectAndTeamWithinSameWorkspace(String projectId, String teamId) {
        ProjectDto projectDto = projectRetrieveService.retrieve(projectId);
        TeamDto teamDto = teamRetrieveService.retrieveTeam(teamId);
        if (!projectDto.getWorkspaceId().equalsIgnoreCase(teamDto.getWorkspaceId()) || ARCHIVED.equals(teamDto.getTeamState())) {
            throw new NoAccessException();
        }
    }
}
