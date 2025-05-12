package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskFeedItemManager;
import co.jinear.core.model.response.task.TaskFeedItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/feed-item")
public class TaskFeedItemController {

    private final TaskFeedItemManager taskFeedItemManager;

    @GetMapping("/from-task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskFeedItemResponse retrieveTaskFeedItems(@PathVariable String taskId) {
        return taskFeedItemManager.retrieveTaskFeedItems(taskId);
    }
}
