package co.jinear.core.controller.task;

import co.jinear.core.manager.reminder.TaskReminderManager;
import co.jinear.core.model.request.reminder.TaskReminderInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.reminder.ReminderJobResponse;
import co.jinear.core.model.response.reminder.ReminderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/task/reminder")
public class TaskReminderController {

    private final TaskReminderManager reminderManager;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReminderResponse initializeTaskReminder(@Valid @RequestBody TaskReminderInitializeRequest reminderInitializeRequest) {
        return reminderManager.initializeTaskReminder(reminderInitializeRequest);
    }

    @DeleteMapping("/{taskReminderId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse passivizeTaskReminder(@PathVariable String taskReminderId) {
        return reminderManager.passivizeTaskReminder(taskReminderId);
    }

    @GetMapping("/job/{taskReminderId}/next")
    @ResponseStatus(HttpStatus.OK)
    public ReminderJobResponse retrieveNextJob(@PathVariable String taskReminderId) {
        return reminderManager.retrieveNextJob(taskReminderId);
    }
}
