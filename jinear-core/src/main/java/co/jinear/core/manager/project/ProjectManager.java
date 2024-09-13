package co.jinear.core.manager.project;

import co.jinear.core.converter.project.ProjectDatesUpdateRequestToVoConverter;
import co.jinear.core.converter.project.ProjectInitializeRequestToVoConverter;
import co.jinear.core.model.dto.project.ProjectDto;
import co.jinear.core.model.request.project.*;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.project.ProjectInitializeVo;
import co.jinear.core.model.vo.project.UpdateProjectDatesVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.project.ProjectOperationService;
import co.jinear.core.service.project.ProjectRetrieveService;
import co.jinear.core.validator.project.ProjectAccessValidator;
import co.jinear.core.validator.team.TeamValidator;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.internal.util.StringHelper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectManager {

    private final SessionInfoService sessionInfoService;
    private final ProjectOperationService projectOperationService;
    private final WorkspaceValidator workspaceValidator;
    private final TeamValidator teamValidator;
    private final ProjectAccessValidator projectAccessValidator;
    private final ProjectRetrieveService projectRetrieveService;
    private final ProjectDatesUpdateRequestToVoConverter projectDatesUpdateRequestToVoConverter;

    private final ProjectInitializeRequestToVoConverter projectInitializeRequestToVoConverter;

    public BaseResponse initializeProject(ProjectInitializeRequest projectInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        workspaceValidator.validateHasAccess(currentAccountId, projectInitializeRequest.getWorkspaceId());
        log.info("Initialize project has started. currentAccountId: {}", currentAccountId);
        validateLeadHasWorkspaceAccess(projectInitializeRequest.getLeadWorkspaceMemberId(), projectInitializeRequest.getWorkspaceId());
        validateTeamIds(projectInitializeRequest);
        ProjectInitializeVo projectInitializeVo = projectInitializeRequestToVoConverter.map(projectInitializeRequest);
        projectOperationService.initialize(projectInitializeVo);
        return new BaseResponse();
    }

    public BaseResponse updateTitle(String projectId, ProjectTitleUpdateRequest projectTitleUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        projectAccessValidator.validateHasExplicitAccess(projectId, currentAccountId);
        log.info("Update project title has started. currentAccountId: {}", currentAccountId);
        projectOperationService.updateTitle(projectId, projectTitleUpdateRequest.getTitle());
        return new BaseResponse();
    }

    public BaseResponse updateDescription(String projectId, ProjectDescriptionUpdateRequest projectDescriptionUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        projectAccessValidator.validateHasExplicitAccess(projectId, currentAccountId);
        log.info("Update project description has started. currentAccountId: {}", currentAccountId);
        projectOperationService.updateDescription(projectId, projectDescriptionUpdateRequest.getDescription());
        return new BaseResponse();
    }

    public BaseResponse updateState(String projectId, ProjectStateUpdateRequest projectStateUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        projectAccessValidator.validateHasExplicitAccess(projectId, currentAccountId);
        log.info("Update project state has started. currentAccountId: {}", currentAccountId);
        projectOperationService.updateState(projectId, projectStateUpdateRequest.getProjectState());
        return new BaseResponse();
    }

    public BaseResponse updatePriority(String projectId, ProjectPriorityUpdateRequest projectPriorityUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        projectAccessValidator.validateHasExplicitAccess(projectId, currentAccountId);
        log.info("Update project priority has started. currentAccountId: {}", currentAccountId);
        projectOperationService.updatePriority(projectId, projectPriorityUpdateRequest.getProjectPriority());
        return new BaseResponse();
    }

    public BaseResponse updateDates(String projectId, ProjectDatesUpdateRequest projectDatesUpdateRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        projectAccessValidator.validateHasExplicitAccess(projectId, currentAccountId);
        log.info("Update project dates has started. currentAccountId: {}", currentAccountId);
        UpdateProjectDatesVo updateProjectDatesVo = projectDatesUpdateRequestToVoConverter.convert(projectDatesUpdateRequest);
        projectOperationService.updateDates(projectId, updateProjectDatesVo);
        return new BaseResponse();
    }

    public BaseResponse updateLead(String projectId, ProjectUpdateLeadRequest projectUpdateLeadRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        projectAccessValidator.validateHasExplicitAccess(projectId, currentAccountId);
        ProjectDto projectDto = projectRetrieveService.retrieve(projectId);
        validateLeadHasWorkspaceAccess(projectUpdateLeadRequest.getWorkspaceMemberId(), projectDto.getWorkspaceId());
        projectOperationService.updateLead(projectId, projectUpdateLeadRequest.getWorkspaceMemberId());
        return new BaseResponse();
    }

    private void validateTeamIds(ProjectInitializeRequest projectInitializeRequest) {
        if (Objects.nonNull(projectInitializeRequest.getTeamIds())) {
            teamValidator.validateAllExistsAndActiveWithinSameWorkspace(projectInitializeRequest.getWorkspaceId(), projectInitializeRequest.getTeamIds());
        }
    }

    private void validateLeadHasWorkspaceAccess(String leadWorkspaceMemberId, String workspaceId) {
        if (StringHelper.isNotEmpty(leadWorkspaceMemberId)) {
            log.info("Validate lead has workspace access has started. leadWorkspaceMemberId: {}, workspaceId: {}", leadWorkspaceMemberId, workspaceId);
            workspaceValidator.validateWorkspaceMemberIdIsInWorkspace(leadWorkspaceMemberId, workspaceId);
        }
    }
}
