package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskBoardListingManager;
import co.jinear.core.model.response.task.TaskBoardListingPaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task-board/list")
public class TaskBoardListingController {

    private final TaskBoardListingManager taskBoardListingManager;

    @GetMapping("/{workspaceId}/team/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskBoardListingPaginatedResponse retrieveAll(@PathVariable String workspaceId,
                                                         @PathVariable String teamId,
                                                         @RequestParam(required = false, defaultValue = "0") Integer page) {
        return taskBoardListingManager.retrieveAll(workspaceId, teamId, page);
    }
}
