package co.jinear.core.controller.task;

import co.jinear.core.manager.reminder.TaskReminderManager;
import co.jinear.core.model.request.reminder.TaskReminderInitializeRequest;
import co.jinear.core.model.response.reminder.ReminderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/reminder/")
public class TaskReminderController {

    private final TaskReminderManager reminderManager;

    @PostMapping("/{taskId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ReminderResponse initializeTaskReminder(@PathVariable String taskId,
                                                   @Valid @RequestBody TaskReminderInitializeRequest reminderInitializeRequest) {
        return reminderManager.initializeTaskReminder(taskId, reminderInitializeRequest);
    }
}
