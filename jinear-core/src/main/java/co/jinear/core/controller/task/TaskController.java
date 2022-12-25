package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskInitializeManager;
import co.jinear.core.manager.task.TaskRetrieveManager;
import co.jinear.core.model.request.task.TaskInitializeRequest;
import co.jinear.core.model.response.task.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task")
public class TaskController {

    private final TaskInitializeManager taskInitializeManager;
    private final TaskRetrieveManager taskRetrieveManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse initializeTask(@Valid @RequestBody TaskInitializeRequest taskInitializeRequest) {
        return taskInitializeManager.initializeTask(taskInitializeRequest);
    }

    @GetMapping("/from-workspace/{workspaceName}/{teamTag}-{tagNo}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse retrieveWithWorkspaceNameAndTeamTagNo(@PathVariable String workspaceName,
                                                              @PathVariable String teamTag,
                                                              @PathVariable Integer tagNo) {
        return taskRetrieveManager.retrieveWithWorkspaceNameAndTeamTagNo(workspaceName, teamTag, tagNo);
    }
}
