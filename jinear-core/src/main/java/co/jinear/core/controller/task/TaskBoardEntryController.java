package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskBoardEntryManager;
import co.jinear.core.model.request.task.TaskBoardEntryFilterRequest;
import co.jinear.core.model.request.task.TaskBoardEntryInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.TaskBoardEntryPaginatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task-board/entry")
public class TaskBoardEntryController {

    private final TaskBoardEntryManager taskBoardEntryManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initializeTaskListEntry(@Valid @RequestBody TaskBoardEntryInitializeRequest taskListInitializeRequest) {
        return taskBoardEntryManager.initializeTaskBoardEntry(taskListInitializeRequest);
    }

    @DeleteMapping("/{taskBoardEntryId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteTaskBoardEntry(@PathVariable String taskBoardEntryId) {
        return taskBoardEntryManager.deleteTaskBoardEntry(taskBoardEntryId);
    }

    @PutMapping("/{taskBoardEntryId}/change-order")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse changeOrder(@PathVariable String taskBoardEntryId,
                                    @RequestParam Integer newOrder) {
        return taskBoardEntryManager.changeOrder(taskBoardEntryId, newOrder);
    }

    @PutMapping("/{taskBoardEntryId}/change-filtered-order")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse changeFilteredOrder(@PathVariable String taskBoardEntryId,
                                            @RequestParam(required = false) String taskBoardEntryIdBefore,
                                            @RequestParam(required = false) String taskBoardEntryIdAfter) {
        return taskBoardEntryManager.changeFilteredOrder(taskBoardEntryId, taskBoardEntryIdBefore, taskBoardEntryIdAfter);
    }

    @GetMapping("/from-task-board/{taskBoardId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskBoardEntryPaginatedResponse retrieveFromTaskBoard(@PathVariable String taskBoardId,
                                                                 @RequestParam(required = false, defaultValue = "0") Integer page) {
        return taskBoardEntryManager.retrieveFromTaskBoard(taskBoardId, page);
    }

    @PostMapping("/from-task-board/{taskBoardId}/filter")
    @ResponseStatus(HttpStatus.OK)
    public TaskBoardEntryPaginatedResponse filterFromTaskBoard(@PathVariable String taskBoardId,
                                                               @RequestBody TaskBoardEntryFilterRequest taskBoardEntryFilterRequest,
                                                               @RequestParam(required = false, defaultValue = "0") Integer page) {
        return taskBoardEntryManager.filterFromTaskBoard(taskBoardId, taskBoardEntryFilterRequest, page);
    }
}
