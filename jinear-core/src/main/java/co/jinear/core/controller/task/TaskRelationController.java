package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskRelationManager;
import co.jinear.core.model.request.task.TaskRelationInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/relation")
public class TaskRelationController {

    private final TaskRelationManager taskRelationManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initializeTaskRelation(@Valid @RequestBody TaskRelationInitializeRequest taskRelationInitializeRequest) {
        return taskRelationManager.initializeTaskRelation(taskRelationInitializeRequest);
    }

    @DeleteMapping("/{relationId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteTaskRelation(@PathVariable String relationId) {
        return taskRelationManager.deleteTaskRelation(relationId);
    }
}
