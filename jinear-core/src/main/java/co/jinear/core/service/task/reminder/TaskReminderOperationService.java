package co.jinear.core.service.task.reminder;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.reminder.ReminderDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.entity.task.TaskReminder;
import co.jinear.core.model.enumtype.reminder.ReminderType;
import co.jinear.core.model.enumtype.reminder.RepeatType;
import co.jinear.core.model.enumtype.task.TaskReminderType;
import co.jinear.core.model.vo.reminder.ReminderInitializeVo;
import co.jinear.core.model.vo.task.InitializeTaskRemindersVo;
import co.jinear.core.repository.task.TaskReminderRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.service.reminder.ReminderOperationService;
import co.jinear.core.service.task.TaskRetrieveService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskReminderOperationService {

    private final TaskRetrieveService taskRetrieveService;
    private final ReminderOperationService reminderOperationService;
    private final TaskReminderRepository taskReminderRepository;
    private final TaskReminderRetrieveService taskReminderRetrieveService;
    private final PassiveService passiveService;

    @Transactional
    public List<ReminderDto> initializeTaskReminders(InitializeTaskRemindersVo initializeTaskRemindersVo) {
        log.info("Initialize task reminders has started. initializeTaskRemindersVo: {}", initializeTaskRemindersVo);
        TaskDto taskDto = taskRetrieveService.retrievePlain(initializeTaskRemindersVo.getTaskId());
        List<ReminderDto> reminders = new ArrayList<>();
        initializeAssignedDateReminder(initializeTaskRemindersVo, taskDto, reminders);
        initializeDueDateReminder(initializeTaskRemindersVo, taskDto, reminders);
        initializeSpecificDateReminder(initializeTaskRemindersVo, taskDto, reminders);
        validateAnyRemindersCreated(reminders);
        return reminders;
    }

    @Transactional
    public void passivizeTaskReminder(String taskReminderId) {
        log.info("Passivize task reminder has started. taskReminderId: {}", taskReminderId);
        TaskReminder taskReminder = taskReminderRetrieveService.retrieveEntity(taskReminderId);
        String passiveId = passivizeTaskReminder(taskReminder);
        reminderOperationService.passivizeReminderAndAllActiveJobsWithExistingPassiveId(taskReminder.getReminderId(), passiveId);
        log.info("Passivize task reminder has finished. passiveId: {}", passiveId);
    }

    private String passivizeTaskReminder(TaskReminder taskReminder) {
        String passiveId = passiveService.createUserActionPassive();
        taskReminder.setPassiveId(passiveId);
        taskReminderRepository.save(taskReminder);
        return passiveId;
    }

    private void initializeAssignedDateReminder(InitializeTaskRemindersVo initializeTaskRemindersVo, TaskDto taskDto, List<ReminderDto> reminders) {
        if (Boolean.TRUE.equals(initializeTaskRemindersVo.getBeforeAssignedDate()) &&
                Objects.nonNull(taskDto.getAssignedDate()) &&
                !hasAnyRelatedTaskReminderExists(taskDto, TaskReminderType.ASSIGNED_DATE)
        ) {
            log.info("Initializing reminder for assigned date. assignedDate: {}", taskDto.getAssignedDate());
            ReminderInitializeVo reminderInitializeVo = mapForAssignedDate(initializeTaskRemindersVo, taskDto);
            ReminderDto reminderDto = initializeReminder(reminderInitializeVo);
            initializeTaskReminder(reminderDto, TaskReminderType.ASSIGNED_DATE);
            reminders.add(reminderDto);
        }
    }

    private void initializeDueDateReminder(InitializeTaskRemindersVo initializeTaskRemindersVo, TaskDto taskDto, List<ReminderDto> reminders) {
        if (Boolean.TRUE.equals(initializeTaskRemindersVo.getBeforeDueDate()) &&
                Objects.nonNull(taskDto.getDueDate()) &&
                !hasAnyRelatedTaskReminderExists(taskDto, TaskReminderType.DUE_DATE)
        ) {
            log.info("Initializing reminder for due date. dueDate: {}", taskDto.getDueDate());
            ReminderInitializeVo reminderInitializeVo = mapForDueDate(initializeTaskRemindersVo, taskDto);
            ReminderDto reminderDto = initializeReminder(reminderInitializeVo);
            initializeTaskReminder(reminderDto, TaskReminderType.DUE_DATE);
            reminders.add(reminderDto);
        }
    }

    private void initializeSpecificDateReminder(InitializeTaskRemindersVo initializeTaskRemindersVo, TaskDto taskDto, List<ReminderDto> reminders) {
        if (Objects.nonNull(initializeTaskRemindersVo.getSpecificRemindDate())) {
            log.info("Initializing reminder for specific date. date: {}", initializeTaskRemindersVo.getSpecificRemindDate());
            ReminderInitializeVo reminderInitializeVo = mapForSpecificDate(initializeTaskRemindersVo, taskDto);
            ReminderDto reminderDto = initializeReminder(reminderInitializeVo);
            initializeTaskReminder(reminderDto, TaskReminderType.SPECIFIC_DATE);
            reminders.add(reminderDto);
        }
    }

    @NonNull
    private ReminderInitializeVo mapForAssignedDate(InitializeTaskRemindersVo initializeTaskRemindersVo, TaskDto taskDto) {
        ReminderInitializeVo reminderInitializeVo = new ReminderInitializeVo();
        reminderInitializeVo.setOwnerId(initializeTaskRemindersVo.getOwnerId());
        reminderInitializeVo.setRelatedObjectId(taskDto.getTaskId());
        reminderInitializeVo.setType(ReminderType.TASK);
        reminderInitializeVo.setRepeatType(RepeatType.NONE);
        reminderInitializeVo.setInitialRemindDate(taskDto.getAssignedDate());
        return reminderInitializeVo;
    }

    @NonNull
    private ReminderInitializeVo mapForDueDate(InitializeTaskRemindersVo initializeTaskRemindersVo, TaskDto taskDto) {
        ReminderInitializeVo reminderInitializeVo = new ReminderInitializeVo();
        reminderInitializeVo.setOwnerId(initializeTaskRemindersVo.getOwnerId());
        reminderInitializeVo.setRelatedObjectId(taskDto.getTaskId());
        reminderInitializeVo.setType(ReminderType.TASK);
        reminderInitializeVo.setRepeatType(RepeatType.NONE);
        reminderInitializeVo.setInitialRemindDate(taskDto.getDueDate());
        return reminderInitializeVo;
    }

    @NonNull
    private ReminderInitializeVo mapForSpecificDate(InitializeTaskRemindersVo initializeTaskRemindersVo, TaskDto taskDto) {
        ReminderInitializeVo reminderInitializeVo = new ReminderInitializeVo();
        reminderInitializeVo.setOwnerId(initializeTaskRemindersVo.getOwnerId());
        reminderInitializeVo.setRelatedObjectId(taskDto.getTaskId());
        reminderInitializeVo.setType(ReminderType.TASK);
        reminderInitializeVo.setRepeatType(initializeTaskRemindersVo.getSpecificRemindDateRepeatType());
        reminderInitializeVo.setInitialRemindDate(initializeTaskRemindersVo.getSpecificRemindDate());
        reminderInitializeVo.setRepeatStart(initializeTaskRemindersVo.getSpecificRemindRepeatStart());
        reminderInitializeVo.setRepeatEnd(initializeTaskRemindersVo.getSpecificRemindRepeatEnd());
        return reminderInitializeVo;
    }

    private ReminderDto initializeReminder(ReminderInitializeVo reminderInitializeVo) {
        return reminderOperationService.initializeReminder(reminderInitializeVo);
    }

    private void initializeTaskReminder(ReminderDto reminderDto, TaskReminderType taskReminderType) {
        log.info("Initialize task reminder has started. reminderDto: {}, taskReminderType: {}", reminderDto, taskReminderType);
        TaskReminder taskReminder = new TaskReminder();
        taskReminder.setTaskId(reminderDto.getRelatedObjectId());
        taskReminder.setReminderId(reminderDto.getReminderId());
        taskReminder.setTaskReminderType(taskReminderType);
        TaskReminder saved = taskReminderRepository.save(taskReminder);
        log.info("Initialize task reminder has completed. taskReminderId: {}", saved.getTaskReminderId());
    }

    private void validateAnyRemindersCreated(List<ReminderDto> reminders) {
        if (reminders.isEmpty()) {
            throw new BusinessException();
        }
    }

    private boolean hasAnyRelatedTaskReminderExists(TaskDto taskDto, TaskReminderType taskReminderType) {
        return taskReminderRetrieveService.hasAnyRelated(taskDto.getTaskId(), taskReminderType);
    }
}
