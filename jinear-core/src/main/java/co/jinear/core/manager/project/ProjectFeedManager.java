package co.jinear.core.manager.project;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.project.*;
import co.jinear.core.model.enumtype.project.ProjectFeedAccessType;
import co.jinear.core.model.response.project.ProjectFeedPaginatedResponse;
import co.jinear.core.model.response.project.ProjectFeedPostResponse;
import co.jinear.core.model.response.project.PublicProjectRetrieveResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.project.ProjectFeedSettingsRetrieveService;
import co.jinear.core.service.project.ProjectPostService;
import co.jinear.core.service.project.ProjectRetrieveService;
import co.jinear.core.service.project.domain.ProjectDomainRetrieveService;
import co.jinear.core.validator.project.ProjectAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectFeedManager {

    private final SessionInfoService sessionInfoService;
    private final ProjectFeedSettingsRetrieveService projectFeedSettingsRetrieveService;
    private final ProjectAccessValidator projectAccessValidator;
    private final ProjectPostService projectPostService;
    private final ProjectRetrieveService projectRetrieveService;
    private final ProjectDomainRetrieveService projectDomainRetrieveService;

    public ProjectFeedPaginatedResponse retrieveFeed(String projectId, int page) {
        String currentAccountId = sessionInfoService.currentAccountIdInclAnonymous();
        ProjectFeedSettingsDto projectFeedSettingsDto = projectFeedSettingsRetrieveService.retrieve(projectId);
        validateAccess(projectId, currentAccountId, projectFeedSettingsDto);
        log.info("Retrieve feed has started. currentAccountId: {}, page: {}", currentAccountId, page);
        Page<ProjectPostDto> projectPostDtos = projectPostService.retrieveFeedPosts(projectId, page);
        return mapResponse(projectPostDtos);
    }

    public ProjectFeedPostResponse retrievePost(String projectId, String postId) {
        String currentAccountId = sessionInfoService.currentAccountIdInclAnonymous();
        ProjectFeedSettingsDto projectFeedSettingsDto = projectFeedSettingsRetrieveService.retrieve(projectId);
        validateAccess(projectId, currentAccountId, projectFeedSettingsDto);
        log.info("Retrieve post has started. projectId: {}, postId: {}, currentAccountId: {}", projectId, postId, currentAccountId);
        ProjectPostDto projectPostDto = projectPostService.retrievePost(projectId, postId);
        return mapResponse(projectPostDto);
    }

    public PublicProjectRetrieveResponse retrievePublicProjectInfoWithDomain(String domain) {
        log.info("Retrieve public project info with domain has started. domain: {}", domain);
        ProjectDomainDto projectDomainDto = projectDomainRetrieveService.retrieveByDomain(domain);
        return retrievePublicProjectInfo(projectDomainDto.getProjectId());
    }

    public PublicProjectRetrieveResponse retrievePublicProjectInfo(String projectId) {
        String currentAccountId = sessionInfoService.currentAccountIdInclAnonymous();
        ProjectFeedSettingsDto projectFeedSettingsDto = projectFeedSettingsRetrieveService.retrieve(projectId);
        validateAccess(projectId, currentAccountId, projectFeedSettingsDto);
        log.info("Retrieve public project info has started. projectId: {}, currentAccountId: {}", projectId, currentAccountId);
        PublicProjectDto publicProjectDto = projectRetrieveService.retrievePublic(projectId);
        AccountProjectPermissionFlags accountProjectPermissionFlags = projectAccessValidator.retrieveAccountProjectPermissionFlags(currentAccountId, projectId);
        publicProjectDto.setAccountProjectPermissionFlags(accountProjectPermissionFlags);
        return mapResponse(publicProjectDto);
    }

    private PublicProjectRetrieveResponse mapResponse(PublicProjectDto publicProjectDto) {
        PublicProjectRetrieveResponse projectFeedPaginatedResponse = new PublicProjectRetrieveResponse();
        projectFeedPaginatedResponse.setPublicProjectDto(publicProjectDto);
        return projectFeedPaginatedResponse;
    }

    private void validateAccess(String projectId, String currentAccountId, ProjectFeedSettingsDto projectFeedSettingsDto) {
        ProjectFeedAccessType projectFeedAccessType = projectFeedSettingsDto.getProjectFeedAccessType();
        switch (projectFeedAccessType) {
            case PRIVATE -> projectAccessValidator.validateHasExplicitAdminAccess(projectId, currentAccountId);
            case GUESTS_ONLY -> throw new NoAccessException();
            case PUBLIC -> log.info("Project feed is public. currentAccountId: {}", currentAccountId);
        }
    }

    private ProjectFeedPaginatedResponse mapResponse(Page<ProjectPostDto> projectPostDtos) {
        ProjectFeedPaginatedResponse projectFeedPaginatedResponse = new ProjectFeedPaginatedResponse();
        projectFeedPaginatedResponse.setPostDtoPageDto(new PageDto<>(projectPostDtos));
        return projectFeedPaginatedResponse;
    }

    private ProjectFeedPostResponse mapResponse(ProjectPostDto projectPostDto) {
        ProjectFeedPostResponse projectFeedPostResponse = new ProjectFeedPostResponse();
        projectFeedPostResponse.setProjectPostDto(projectPostDto);
        return projectFeedPostResponse;
    }
}
