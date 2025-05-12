package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskBoardListingManager;
import co.jinear.core.model.response.task.TaskBoardRetrieveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task-board")
public class TaskBoardRetrieveController {

    private final TaskBoardListingManager taskBoardListingManager;

    @GetMapping("/{taskBoardId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskBoardRetrieveResponse retrieve(@PathVariable String taskBoardId) {
        return taskBoardListingManager.retrieve(taskBoardId);
    }
}
