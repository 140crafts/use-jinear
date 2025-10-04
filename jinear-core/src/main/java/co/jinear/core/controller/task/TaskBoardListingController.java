package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskBoardListingManager;
import co.jinear.core.model.response.task.TaskAndTaskBoardRelationResponse;
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
    public TaskBoardListingPaginatedResponse retrieveAllByTeam(@PathVariable String workspaceId,
                                                               @PathVariable String teamId,
                                                               @RequestParam(required = false, defaultValue = "0") Integer page) {
        return taskBoardListingManager.retrieveAllByTeam(workspaceId, teamId, page);
    }

    @GetMapping("/{workspaceId}/filter")
    @ResponseStatus(HttpStatus.OK)
    public TaskBoardListingPaginatedResponse filterAllByName(@PathVariable String workspaceId,
                                                             @RequestParam(required = false, defaultValue = "") String name,
                                                             @RequestParam(required = false, defaultValue = "0") Integer page) {
        return taskBoardListingManager.filterAllByName(workspaceId, name, page);
    }

    @GetMapping("/{workspaceId}/team/{teamId}/filter")
    @ResponseStatus(HttpStatus.OK)
    public TaskBoardListingPaginatedResponse filterAllByTeamAndName(@PathVariable String workspaceId,
                                                                    @PathVariable String teamId,
                                                                    @RequestParam(required = false, defaultValue = "") String name,
                                                                    @RequestParam(required = false, defaultValue = "0") Integer page) {
        return taskBoardListingManager.filterAllByTeamAndName(workspaceId, teamId, name, page);
    }

    @GetMapping("/related-with-task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskAndTaskBoardRelationResponse retrieveTaskAndTaskBoardsRelation(@PathVariable String taskId,
                                                                              @RequestParam(required = false, defaultValue = "") String filterRecentsByName) {
        return taskBoardListingManager.retrieveTasksBoardAndRecentBoards(taskId, filterRecentsByName);
    }


}
