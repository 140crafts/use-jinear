package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskSubscriptionManager;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.task.TaskSubscribersListingResponse;
import co.jinear.core.model.response.task.TaskSubscriptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/{taskId}/subscription")
public class TaskSubscriptionController {

    private final TaskSubscriptionManager taskSubscriptionManager;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TaskSubscriptionResponse retrieveSubscription(@PathVariable String taskId) {
        return taskSubscriptionManager.retrieveTaskSubscription(taskId);
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public TaskSubscribersListingResponse retrieveAllSubscribers(@PathVariable String taskId) {
        return taskSubscriptionManager.retrieveAllSubscribers(taskId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskSubscriptionResponse initializeTaskSubscription(@PathVariable String taskId) {
        return taskSubscriptionManager.initializeTaskSubscription(taskId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse removeTaskSubscription(@PathVariable String taskId) {
        return taskSubscriptionManager.removeTaskSubscription(taskId);
    }
}
