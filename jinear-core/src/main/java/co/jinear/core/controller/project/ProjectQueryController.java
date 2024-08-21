package co.jinear.core.controller.project;

import co.jinear.core.manager.project.ProjectQueryManager;
import co.jinear.core.model.response.project.ProjectListingPaginatedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/project/query")
public class ProjectQueryController {

    private final ProjectQueryManager projectQueryManager;

    @GetMapping("/assigned/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectListingPaginatedResponse retrieveAssigned(@PathVariable String workspaceId,
                                                            @RequestParam(required = false, defaultValue = "0") Integer page) {
        return projectQueryManager.retrieveAssigned(workspaceId, page);
    }

    @GetMapping("/all/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectListingPaginatedResponse retrieveAll(@PathVariable String workspaceId,
                                                       @RequestParam(required = false, defaultValue = "0") Integer page) {
        return projectQueryManager.retrieveAll(workspaceId, page);
    }
}
