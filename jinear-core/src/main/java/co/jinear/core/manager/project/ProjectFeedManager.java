package co.jinear.core.manager.project;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.project.ProjectFeedSettingsDto;
import co.jinear.core.model.dto.project.ProjectPostDto;
import co.jinear.core.model.enumtype.project.ProjectFeedAccessType;
import co.jinear.core.model.response.project.ProjectFeedPaginatedResponse;
import co.jinear.core.model.response.project.ProjectFeedPostResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.project.ProjectFeedSettingsRetrieveService;
import co.jinear.core.service.project.ProjectPostService;
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

    public ProjectFeedPaginatedResponse retrieveFeed(String projectId, int page) {
        String currentAccountId = sessionInfoService.currentAccountIdInclAnonymous();
        validateAccess(projectId, currentAccountId);
        log.info("Retrieve feed has started. currentAccountId: {}, page: {}", currentAccountId, page);
        Page<ProjectPostDto> projectPostDtos = projectPostService.retrieveFeedPosts(projectId, page);
        return mapResponse(projectPostDtos);
    }

    public ProjectFeedPostResponse retrievePost(String projectId, String postId) {
        String currentAccountId = sessionInfoService.currentAccountIdInclAnonymous();
        validateAccess(projectId, currentAccountId);
        log.info("Retrieve post has started. projectId: {}, postId: {}, currentAccountId: {}", projectId, postId, currentAccountId);
        ProjectPostDto projectPostDto = projectPostService.retrievePost(projectId, postId);
        return mapResponse(projectPostDto);
    }

    private void validateAccess(String projectId, String currentAccountId) {
        ProjectFeedSettingsDto projectFeedSettingsDto = projectFeedSettingsRetrieveService.retrieve(projectId);
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
