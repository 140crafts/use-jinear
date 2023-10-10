package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskCommentManager;
import co.jinear.core.model.request.task.InitializeTaskCommentRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.PaginatedTaskCommentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/comment")
public class TaskCommentController {

    private final TaskCommentManager taskCommentManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse initializeTaskComment(@Valid @RequestBody InitializeTaskCommentRequest initializeTaskCommentRequest) {
        return taskCommentManager.initializeComment(initializeTaskCommentRequest);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse deleteTaskComment(@PathVariable String commentId) {
        return taskCommentManager.deleteComment(commentId);
    }

    @GetMapping("/from-task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public PaginatedTaskCommentResponse retrieveTaskComments(@PathVariable String taskId,
                                                             @RequestParam(required = false, defaultValue = "0") Integer page) {
        return taskCommentManager.retrieveTaskComments(taskId, page);
    }
}
