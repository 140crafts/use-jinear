package co.jinear.core.manager.project;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.project.ProjectFeedSettingsDto;
import co.jinear.core.model.dto.project.ProjectPostCommentDto;
import co.jinear.core.model.dto.project.ProjectPostDto;
import co.jinear.core.model.enumtype.project.ProjectFeedAccessType;
import co.jinear.core.model.enumtype.project.ProjectPostCommentPolicyType;
import co.jinear.core.model.request.project.ProjectPostAddCommentRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.project.ProjectPostPaginatedCommentResponse;
import co.jinear.core.model.vo.project.ProjectPostInitializeVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.project.*;
import co.jinear.core.validator.project.ProjectAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectPostCommentManager {

    private final SessionInfoService sessionInfoService;
    private final ProjectFeedSettingsRetrieveService projectFeedSettingsRetrieveService;
    private final ProjectPostService projectPostService;
    private final ProjectPostCommentOperationService projectPostCommentOperationService;
    private final ProjectPostCommentRetrieveService projectPostCommentRetrieveService;
    private final ProjectPostCommentListingService projectPostCommentListingService;
    private final ProjectAccessValidator projectAccessValidator;
    private final PassiveService passiveService;

    public BaseResponse addComment(ProjectPostAddCommentRequest projectPostAddCommentRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validatePostAndProjectMatchAndCommentSettings(projectPostAddCommentRequest, currentAccountId);
        log.info("Add project feed post comment has started. currentAccountId: {}", currentAccountId);
        initializeComment(projectPostAddCommentRequest, currentAccountId);
        return new BaseResponse();
    }

    public BaseResponse deleteComment(String commentId) {
        String currentAccountId = sessionInfoService.currentAccountId();
        validateDeleteAccess(commentId, currentAccountId);
        log.info("Delete project feed post comment has started. currentAccountId: {}", currentAccountId);
        String passiveId = projectPostCommentOperationService.deleteComment(commentId);
        passiveService.assignOwnership(passiveId, currentAccountId);
        return new BaseResponse();
    }

    public ProjectPostPaginatedCommentResponse listComments(String postId, String projectId, Integer page) {
        String currentAccountId = sessionInfoService.currentAccountIdInclAnonymous();
        ProjectPostDto projectPostDto = projectPostService.retrievePost(projectId, postId);
        validateCommentListingAccess(projectPostDto.getProjectId(), currentAccountId);
        log.info("List comments has started. postId: {}, currentAccountId: {}, page: {}", postId, currentAccountId, page);
        Page<ProjectPostCommentDto> projectPostCommentDtoPage = projectPostCommentListingService.retrievePostComments(postId, page);
        return mapResponse(projectPostCommentDtoPage);
    }

    private void validatePostAndProjectMatchAndCommentSettings(ProjectPostAddCommentRequest projectPostAddCommentRequest, String currentAccountId) {
        ProjectPostDto projectPostDto = projectPostService.retrievePost(projectPostAddCommentRequest.getProjectId(), projectPostAddCommentRequest.getPostId());
        ProjectFeedSettingsDto projectFeedSettingsDto = projectFeedSettingsRetrieveService.retrieve(projectPostDto.getProjectId());
        validateCommentPolicyType(currentAccountId, projectPostDto, projectFeedSettingsDto, projectPostDto.getProjectId());
    }

    private void validateCommentPolicyType(String currentAccountId, ProjectPostDto projectPostDto, ProjectFeedSettingsDto projectFeedSettingsDto, String projectId) {
        ProjectPostCommentPolicyType projectPostCommentPolicyType = projectFeedSettingsDto.getProjectPostCommentPolicyType();

        switch (projectPostCommentPolicyType) {
            case PROJECT_TEAM_MEMBERS -> projectAccessValidator.validateHasExplicitAccess(projectId, currentAccountId);
            case PROJECT_TEAM_ADMINS ->
                    projectAccessValidator.validateHasExplicitAdminAccess(projectId, currentAccountId);
            case WORKSPACE_ADMINS -> projectAccessValidator.validateWorkspaceAdminOrOwner(projectId, currentAccountId);
            case PROJECT_LEAD -> projectAccessValidator.validateProjectLead(projectId, currentAccountId);
            case WORKSPACE_MEMBERS ->
                    projectAccessValidator.validateProjectWorkspaceMember(projectId, currentAccountId);
            case ANY_LOGGED_IN_USER ->
                    log.info("Any logged in user commenting. currentAccountId: {}, postId: {}", currentAccountId, projectPostDto.getProjectPostId());
            default -> throw new NoAccessException();
        }
    }

    private void initializeComment(ProjectPostAddCommentRequest projectPostAddCommentRequest, String currentAccountId) {
        ProjectPostInitializeVo projectPostInitializeVo = new ProjectPostInitializeVo();
        projectPostInitializeVo.setProjectPostId(projectPostAddCommentRequest.getPostId());
        projectPostInitializeVo.setAccountId(currentAccountId);
        projectPostInitializeVo.setCommentBody(projectPostAddCommentRequest.getBody());
        projectPostInitializeVo.setQuoteProjectPostCommentId(projectPostAddCommentRequest.getQuoteId());
        projectPostCommentOperationService.initialize(projectPostInitializeVo);
    }

    private void validateDeleteAccess(String commentId, String currentAccountId) {
        ProjectPostCommentDto projectPostCommentDto = projectPostCommentRetrieveService.retrieve(commentId);
        String commentOwner = projectPostCommentDto.getAccountId();
        if (!StringUtils.equalsIgnoreCase(commentOwner, currentAccountId)) {
            ProjectPostDto projectPostDto = projectPostService.retrievePost(projectPostCommentDto.getProjectPostId());
            projectAccessValidator.validateHasExplicitAdminAccess(projectPostDto.getProjectId(), currentAccountId);
        }
    }

    private void validateCommentListingAccess(String projectId, String currentAccountId) {
        ProjectFeedSettingsDto projectFeedSettingsDto = projectFeedSettingsRetrieveService.retrieve(projectId);
        ProjectFeedAccessType projectFeedAccessType = projectFeedSettingsDto.getProjectFeedAccessType();
        switch (projectFeedAccessType) {
            case PRIVATE -> projectAccessValidator.validateHasExplicitAdminAccess(projectId, currentAccountId);
            case GUESTS_ONLY -> throw new NoAccessException();
            case PUBLIC -> log.info("Project feed is public. currentAccountId: {}", currentAccountId);
        }
    }

    private ProjectPostPaginatedCommentResponse mapResponse(Page<ProjectPostCommentDto> projectPostCommentDtoPage) {
        ProjectPostPaginatedCommentResponse projectPostPaginatedCommentResponse = new ProjectPostPaginatedCommentResponse();
        projectPostPaginatedCommentResponse.setProjectPostCommentDtoPageDto(new PageDto<>(projectPostCommentDtoPage));
        return projectPostPaginatedCommentResponse;
    }
}
