package co.jinear.core.controller.task;

import co.jinear.core.manager.task.TaskUpdateManager;
import co.jinear.core.model.response.task.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/topic")
public class TaskTopicController {

    private final TaskUpdateManager taskUpdateManager;

    @PutMapping("/{taskId}/{topicId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse updateTaskTopic(@PathVariable String taskId,
                                        @PathVariable String topicId) {
        return taskUpdateManager.updateTaskTopic(taskId, topicId);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse removeTaskTopic(@PathVariable String taskId) {
        return taskUpdateManager.removeTaskTopic(taskId);
    }
}
