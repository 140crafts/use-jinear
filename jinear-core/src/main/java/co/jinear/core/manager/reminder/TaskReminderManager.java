package co.jinear.core.manager.reminder;

import co.jinear.core.converter.reminder.ReminderConverter;
import co.jinear.core.model.dto.reminder.ReminderDto;
import co.jinear.core.model.request.reminder.TaskReminderInitializeRequest;
import co.jinear.core.model.response.reminder.ReminderResponse;
import co.jinear.core.model.vo.task.InitializeTaskRemindersVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.reminder.TaskReminderInitializeService;
import co.jinear.core.validator.task.TaskAccessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskReminderManager {

    private final ReminderConverter reminderConverter;
    private final SessionInfoService sessionInfoService;
    private final TaskAccessValidator taskAccessValidator;
    private final TaskReminderInitializeService taskReminderInitializeService;

    public ReminderResponse initializeTaskReminder(String taskId, TaskReminderInitializeRequest reminderInitializeRequest) {
        String currentAccountId = sessionInfoService.currentAccountId();
        taskAccessValidator.validateTaskAccess(currentAccountId, taskId);
        log.info("Initialize task reminder has started. currentAccountId: {}, taskId: {}", currentAccountId, taskId);
        InitializeTaskRemindersVo initializeTaskRemindersVo = reminderConverter.map(reminderInitializeRequest, taskId, currentAccountId);
        List<ReminderDto> reminders = taskReminderInitializeService.initializeTaskReminders(initializeTaskRemindersVo);
        return mapResponse(reminders);
    }

    private ReminderResponse mapResponse(List<ReminderDto> reminders) {
        ReminderResponse response = new ReminderResponse();
        response.setReminders(reminders);
        return response;
    }

}
