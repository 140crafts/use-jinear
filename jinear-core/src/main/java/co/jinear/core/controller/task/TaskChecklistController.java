package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskChecklistManager;
import co.jinear.core.model.request.task.InitializeChecklistRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.RetrieveChecklistResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/{taskId}/checklist")
public class TaskChecklistController {

    private final TaskChecklistManager taskChecklistManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initializeChecklist(@PathVariable String taskId,
                                            @RequestBody InitializeChecklistRequest initializeChecklistRequest) {
        return taskChecklistManager.initializeChecklist(taskId, initializeChecklistRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RetrieveChecklistResponse retrieveChecklist(@PathVariable String taskId) {
        return taskChecklistManager.retrieveWithTaskId(taskId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse passivizeChecklist(@PathVariable String taskId) {
        return taskChecklistManager.passivizeChecklist(taskId);
    }
}
