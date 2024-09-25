package co.jinear.core.controller.project;

import co.jinear.core.manager.project.ProjectFeedManager;
import co.jinear.core.model.response.project.ProjectFeedPaginatedResponse;
import co.jinear.core.model.response.project.ProjectFeedPostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/project/feed")
public class ProjectFeedController {

    private final ProjectFeedManager projectFeedManager;

    @GetMapping("/{projectId}/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectFeedPostResponse retrieve(@PathVariable String projectId,
                                            @PathVariable String postId) {
        return projectFeedManager.retrievePost(projectId, postId);
    }

    @GetMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectFeedPaginatedResponse list(@PathVariable String projectId,
                                             @RequestParam(required = false, defaultValue = "0") int page) {
        return projectFeedManager.retrieveFeed(projectId, page);
    }
}
