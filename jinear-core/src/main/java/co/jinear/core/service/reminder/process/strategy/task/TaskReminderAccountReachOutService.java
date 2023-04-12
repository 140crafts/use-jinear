package co.jinear.core.service.reminder.process.strategy.task;

import co.jinear.core.converter.reminder.TaskReminderMailVoConverter;
import co.jinear.core.converter.reminder.TaskReminderNotificationConverter;
import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskReminderDto;
import co.jinear.core.model.dto.task.TaskSubscriptionDto;
import co.jinear.core.model.vo.mail.TaskReminderMailVo;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import co.jinear.core.service.mail.MailService;
import co.jinear.core.service.notification.NotificationFireService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskReminderAccountReachOutService {

    private final TaskReminderMailVoConverter taskReminderMailVoConverter;
    private final MailService mailService;
    private final NotificationFireService notificationFireService;
    private final TaskReminderNotificationConverter taskReminderNotificationConverter;

    public void notify(List<TaskSubscriptionDto> taskSubscribers, TaskDto taskDto, TaskReminderDto taskReminderDto, ReminderJobDto reminderJobDto) {
        sendMail(taskSubscribers, taskDto, taskReminderDto, reminderJobDto);
        sendNotification(taskSubscribers, taskDto, taskReminderDto);
    }

    private void sendMail(List<TaskSubscriptionDto> taskSubscribers, TaskDto taskDto, TaskReminderDto taskReminderDto, ReminderJobDto reminderJobDto) {
        Set<TaskReminderMailVo> receiverSet = new HashSet<>();

        taskSubscribers
                .stream()
                .map(TaskSubscriptionDto::getPlainAccountProfileDto)
                .map(acc -> taskReminderMailVoConverter.map(acc, taskDto, taskReminderDto, reminderJobDto))
                .forEach(receiverSet::add);

        Optional.of(taskDto)
                .map(TaskDto::getOwner)
                .map(acc -> taskReminderMailVoConverter.map(acc, taskDto, taskReminderDto, reminderJobDto))
                .ifPresent(receiverSet::add);

        Optional.of(taskDto)
                .map(TaskDto::getAssignedToAccount)
                .map(acc -> taskReminderMailVoConverter.map(acc, taskDto, taskReminderDto, reminderJobDto))
                .ifPresent(receiverSet::add);

        receiverSet.forEach(this::notifyAccountViaMail);
    }

    private void sendNotification(List<TaskSubscriptionDto> taskSubscribers, TaskDto taskDto, TaskReminderDto taskReminderDto) {
        Set<NotificationSendVo> receiverSet = new HashSet<>();

        taskSubscribers
                .stream()
                .map(TaskSubscriptionDto::getPlainAccountProfileDto)
                .map(plainAccountProfileDto -> taskReminderNotificationConverter.mapNotificationSendVo(plainAccountProfileDto.getAccountId(), plainAccountProfileDto.getLocaleType(), taskDto, taskReminderDto))
                .forEach(receiverSet::add);

        Optional.of(taskDto)
                .map(TaskDto::getOwner)
                .map(acc -> taskReminderNotificationConverter.mapNotificationSendVo(acc.getAccountId(), acc.getLocaleType(), taskDto, taskReminderDto))
                .ifPresent(receiverSet::add);

        Optional.of(taskDto)
                .map(TaskDto::getAssignedToAccount)
                .map(acc -> taskReminderNotificationConverter.mapNotificationSendVo(acc.getAccountId(), acc.getLocaleType(), taskDto, taskReminderDto))
                .ifPresent(receiverSet::add);

        receiverSet.forEach(this::notifyAccountViaPushNotif);
    }

    private void notifyAccountViaMail(TaskReminderMailVo taskReminderMailVo) {
        try {
            log.info("Task reminder notify account with mail has started. taskReminderMailVo: {}", taskReminderMailVo);
            mailService.sendTaskReminderMail(taskReminderMailVo);
            log.info("Task reminder notify account with mail has completed.");
        } catch (Exception e) {
            log.error("Task reminder notify account with mail has failed. ", e);
        }
    }

    private void notifyAccountViaPushNotif(NotificationSendVo notificationSendVo) {
        try {
            log.info("Task reminder notify account with push notif has started. notificationSendVo: {}", notificationSendVo);
            notificationFireService.fire(notificationSendVo);
            log.info("Task reminder notify account with push notif has completed.");
        } catch (Exception e) {
            log.error("Task reminder notify account with push notif has failed. ", e);
        }
    }
}
