package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskListingManager;
import co.jinear.core.model.request.task.TaskRetrieveIntersectingRequest;
import co.jinear.core.model.response.task.TaskListingPaginatedResponse;
import co.jinear.core.model.response.task.TaskListingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/list")
public class TaskListingController {

    private final TaskListingManager taskListingManager;

    @GetMapping("/{workspaceId}/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskListingPaginatedResponse retrieveAllTasks(@PathVariable String workspaceId,
                                                         @PathVariable String teamId,
                                                         @RequestParam(required = false, defaultValue = "0") Integer page) {
        return taskListingManager.retrieveAllTasks(workspaceId, teamId, page);
    }

    @GetMapping("/{workspaceId}/{teamId}/intersecting/{start}/{end}")
    @ResponseStatus(HttpStatus.OK)
    public TaskListingResponse retrieveAllIntersectingTasks(@PathVariable String workspaceId,
                                                            @PathVariable String teamId,
                                                            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime start,
                                                            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime end) {
        return taskListingManager.retrieveAllIntersectingTasks(new TaskRetrieveIntersectingRequest(workspaceId, teamId, start, end));
    }
}
