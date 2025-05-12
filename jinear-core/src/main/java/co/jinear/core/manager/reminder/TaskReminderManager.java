package co.jinear.core.manager.reminder;

import co.jinear.core.converter.reminder.ReminderConverter;
import co.jinear.core.model.dto.reminder.ReminderDto;
import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.dto.task.TaskReminderDto;
import co.jinear.core.model.request.reminder.TaskReminderInitializeRequest;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.model.response.reminder.ReminderJobResponse;
import co.jinear.core.model.response.reminder.ReminderResponse;
import co.jinear.core.model.vo.task.InitializeTaskRemindersVo;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.task.reminder.TaskReminderOperationService;
import co.jinear.core.service.task.reminder.TaskReminderRetrieveService;
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
    private final TaskReminderOperationService taskReminderOperationService;
    private final TaskReminderRetrieveService taskReminderRetrieveService;

    public ReminderResponse initializeTaskReminder(TaskReminderInitializeRequest reminderInitializeRequest) {
        final String taskId = reminderInitializeRequest.getTaskId();
        String currentAccountId = sessionInfoService.currentAccountId();
        taskAccessValidator.validateTaskAccess(currentAccountId, taskId);
        log.info("Initialize task reminder has started. currentAccountId: {}, taskId: {}", currentAccountId, taskId);
        InitializeTaskRemindersVo initializeTaskRemindersVo = reminderConverter.map(reminderInitializeRequest, taskId, currentAccountId);
        List<ReminderDto> reminders = taskReminderOperationService.initializeTaskReminders(initializeTaskRemindersVo);
        return mapResponse(reminders);
    }

    public BaseResponse passivizeTaskReminder(String taskReminderId) {
        final String currentAccountId = sessionInfoService.currentAccountId();
        TaskReminderDto taskReminderDto = taskReminderRetrieveService.retrieve(taskReminderId);
        taskAccessValidator.validateTaskAccess(currentAccountId, taskReminderDto.getTaskId());
        log.info("Passivize task reminder has started. currentAccountId: {}", currentAccountId);
        taskReminderOperationService.passivizeTaskReminder(taskReminderId);
        return new BaseResponse();
    }

    public ReminderJobResponse retrieveNextJob(String taskReminderId) {
        final String currentAccountId = sessionInfoService.currentAccountId();
        TaskReminderDto taskReminderDto = taskReminderRetrieveService.retrieve(taskReminderId);
        taskAccessValidator.validateTaskAccess(currentAccountId, taskReminderDto.getTaskId());
        log.info("Retrieve next reminder job has started. currentAccountId: {}, taskReminderId: {}", currentAccountId, taskReminderId);
        ReminderJobDto reminderJobDto = taskReminderRetrieveService.retrieveNextPendingReminderJob(taskReminderId);
        return mapResponse(reminderJobDto);
    }

    private ReminderResponse mapResponse(List<ReminderDto> reminders) {
        ReminderResponse response = new ReminderResponse();
        response.setReminders(reminders);
        return response;
    }

    private ReminderJobResponse mapResponse(ReminderJobDto reminderJobDto) {
        ReminderJobResponse response = new ReminderJobResponse();
        response.setReminderJobDto(reminderJobDto);
        return response;
    }
}
