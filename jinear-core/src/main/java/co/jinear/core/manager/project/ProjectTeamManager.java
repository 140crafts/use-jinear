package co.jinear.core.manager.project;

import co.jinear.core.model.dto.project.ProjectDto;
import co.jinear.core.model.request.project.ProjectTeamOperationRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.project.ProjectRetrieveService;
import co.jinear.core.service.project.ProjectTeamOperationService;
import co.jinear.core.service.team.TeamRetrieveService;
import co.jinear.core.validator.project.ProjectAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    public BaseResponse updateAs(ProjectTeamOperationRequest projectTeamOperationRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        projectAccessValidator.validateHasExplicitAccess(projectTeamOperationRequest.getProjectId(), currentAccountId);
        validateProjectAndTeamWithinSameWorkspace(projectTeamOperationRequest);
        log.info("Update project teams as has started. currentAccountId: {}", currentAccountId);
        String passiveId = projectTeamOperationService.updateAs(projectTeamOperationRequest.getProjectId(), projectTeamOperationRequest.getTeamIds());
        assignPassiveIdOwnershipIfExists(currentAccountId, passiveId);
        return new BaseResponse();
    }

    private void assignPassiveIdOwnershipIfExists(String currentAccountId, String passiveId) {
        if (Objects.nonNull(passiveId)) {
            passiveService.assignOwnership(passiveId, currentAccountId);
        }
    }

    private void validateProjectAndTeamWithinSameWorkspace(ProjectTeamOperationRequest projectTeamOperationRequest) {
        String projectId = projectTeamOperationRequest.getProjectId();
        ProjectDto projectDto = projectRetrieveService.retrieve(projectId);
        teamRetrieveService.checkAllExistsAndActiveWithinSameWorkspace(projectDto.getWorkspaceId(), projectTeamOperationRequest.getTeamIds());
    }
}
