package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskListEntryManager;
import co.jinear.core.model.request.task.TaskListEntryInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.TaskListEntryPaginatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task-list/entry")
public class TaskListEntryController {

    private final TaskListEntryManager taskListEntryManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initializeTaskListEntry(@Valid @RequestBody TaskListEntryInitializeRequest taskListInitializeRequest) {
        return taskListEntryManager.initializeTaskListEntry(taskListInitializeRequest);
    }

    @DeleteMapping("/{taskListEntryId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteTaskListEntry(@PathVariable String taskListEntryId) {
        return taskListEntryManager.deleteTaskListEntry(taskListEntryId);
    }

    @PutMapping("/{taskListEntryId}/change-order")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse changeOrder(@PathVariable String taskListEntryId,
                                    @RequestParam Integer newOrder) {
        return taskListEntryManager.changeOrder(taskListEntryId, newOrder);
    }

    @GetMapping("/from-task-list/{taskListId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskListEntryPaginatedResponse retrieveFromTaskList(@PathVariable String taskListId,
                                                               @RequestParam(required = false, defaultValue = "0") Integer page) {
        return taskListEntryManager.retrieveFromTaskList(taskListId, page);
    }
}
