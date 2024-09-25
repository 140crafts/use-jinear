package co.jinear.core.manager.project;

import co.jinear.core.converter.project.InitializeProjectPostVoConverter;
import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.project.ProjectFeedSettingsDto;
import co.jinear.core.model.dto.project.ProjectPostDto;
import co.jinear.core.model.enumtype.project.ProjectPostInitializeAccessType;
import co.jinear.core.model.request.project.ProjectPostInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.vo.project.InitializeProjectPostVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.project.ProjectFeedSettingsRetrieveService;
import co.jinear.core.service.project.ProjectPostService;
import co.jinear.core.validator.project.ProjectAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectPostManager {

    private final SessionInfoService sessionInfoService;
    private final ProjectFeedSettingsRetrieveService projectFeedSettingsRetrieveService;
    private final ProjectAccessValidator projectAccessValidator;
    private final ProjectPostService projectPostService;
    private final InitializeProjectPostVoConverter initializeProjectPostVoConverter;
    private final PassiveService passiveService;

    public BaseResponse initialize(ProjectPostInitializeRequest projectPostInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        ProjectFeedSettingsDto projectFeedSettingsDto = projectFeedSettingsRetrieveService.retrieve(projectPostInitializeRequest.getProjectId());
        validatePostInitializeAccess(projectPostInitializeRequest, currentAccountId, projectFeedSettingsDto);
        InitializeProjectPostVo initializeProjectPostVo = initializeProjectPostVoConverter.convert(projectPostInitializeRequest, currentAccountId);
        projectPostService.initialize(initializeProjectPostVo);
        return new BaseResponse();
    }

    public BaseResponse delete(String projectId, String projectPostId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateDeleteAccess(projectId, projectPostId, currentAccountId);
        String passiveId = projectPostService.deletePost(projectId, projectPostId);
        passiveService.assignOwnership(passiveId, currentAccountId);
        return new BaseResponse();
    }

    //add - post media
    //remove - post media

    private void validateDeleteAccess(String projectId, String projectPostId, String currentAccountId) {
        ProjectPostDto projectPostDto = projectPostService.retrievePost(projectId, projectPostId);
        if (!StringUtils.equalsIgnoreCase(projectPostDto.getAccountId(), currentAccountId)) {
            log.info("Current account is not post owner. Checking if workspace admin or is team admin any of project teams. projectId: {},projectPostId: {},currentAccountId: {}", projectId, projectPostId, currentAccountId);
            projectAccessValidator.validateHasExplicitAdminAccess(projectId, currentAccountId);
        }
    }

    private void validatePostInitializeAccess(ProjectPostInitializeRequest projectPostInitializeRequest, String currentAccountId, ProjectFeedSettingsDto projectFeedSettingsDto) {
        ProjectPostInitializeAccessType projectPostInitializeAccessType = projectFeedSettingsDto.getProjectPostInitializeAccessType();

        switch (projectPostInitializeAccessType) {
            case PROJECT_TEAM_MEMBERS ->
                    projectAccessValidator.validateHasExplicitAccess(projectPostInitializeRequest.getProjectId(), currentAccountId);
            case PROJECT_TEAM_ADMINS ->
                    projectAccessValidator.validateHasExplicitAdminAccess(projectPostInitializeRequest.getProjectId(), currentAccountId);
            case WORKSPACE_ADMINS ->
                    projectAccessValidator.validateWorkspaceAdminOrOwner(projectPostInitializeRequest.getProjectId(), currentAccountId);
            case PROJECT_LEAD ->
                    projectAccessValidator.validateProjectLead(projectPostInitializeRequest.getProjectId(), currentAccountId);
            case WORKSPACE_MEMBERS ->
                    projectAccessValidator.validateProjectWorkspaceMember(projectPostInitializeRequest.getProjectId(), currentAccountId);
            default -> throw new NoAccessException();
        }
    }
}
