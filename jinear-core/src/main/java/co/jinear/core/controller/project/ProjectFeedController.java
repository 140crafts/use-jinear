package co.jinear.core.controller.project;

import co.jinear.core.manager.project.ProjectFeedManager;
import co.jinear.core.model.response.project.ProjectFeedPaginatedResponse;
import co.jinear.core.model.response.project.ProjectFeedPostResponse;
import co.jinear.core.model.response.project.PublicProjectRetrieveResponse;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/project/public-feed")
public class ProjectFeedController {

    private final ProjectFeedManager projectFeedManager;

    @Validated
    @GetMapping("/{projectId}/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectFeedPostResponse retrieve(@PathVariable @Size(max = 26) String projectId,
                                            @PathVariable @Size(max = 26) String postId) {
        return projectFeedManager.retrievePost(projectId, postId);
    }

    @Validated
    @GetMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectFeedPaginatedResponse list(@PathVariable @Size(max = 26) String projectId,
                                             @RequestParam(required = false, defaultValue = "0") int page) {
        return projectFeedManager.retrieveFeed(projectId, page);
    }

    @Validated
    @GetMapping("/{projectId}/info")
    @ResponseStatus(HttpStatus.OK)
    public PublicProjectRetrieveResponse retrievePublicProject(@PathVariable @Size(max = 26) String projectId) {
        return projectFeedManager.retrievePublicProjectInfo(projectId);
    }

    @Validated
    @GetMapping("/custom-domain/{domain}/info")
    @ResponseStatus(HttpStatus.OK)
    public PublicProjectRetrieveResponse retrievePublicProjectWithDomain(@PathVariable @Size(max = 512) String domain) {
        return projectFeedManager.retrievePublicProjectInfoWithDomain(domain);
    }
}
