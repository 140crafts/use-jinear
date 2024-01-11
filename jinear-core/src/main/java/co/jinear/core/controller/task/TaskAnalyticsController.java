package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskAnalyticsManager;
import co.jinear.core.model.response.task.TaskNumbersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task-analytics")
public class TaskAnalyticsController {

    private final TaskAnalyticsManager taskAnalyticsManager;

    @GetMapping("/{workspaceId}/team/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskNumbersResponse retrieveTaskNumbers(@PathVariable String workspaceId,
                                                   @PathVariable String teamId) {
        return taskAnalyticsManager.retrieveTaskNumbers(workspaceId, teamId);
    }
}
