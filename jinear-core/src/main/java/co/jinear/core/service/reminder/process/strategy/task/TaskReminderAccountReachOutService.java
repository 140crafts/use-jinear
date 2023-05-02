package co.jinear.core.service.reminder.process.strategy.task;

import co.jinear.core.converter.reminder.TaskReminderMailVoMapper;
import co.jinear.core.converter.reminder.TaskReminderNotificationConverter;
import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskReminderDto;
import co.jinear.core.model.dto.task.TaskSubscriptionWithCommunicationPreferencesDto;
import co.jinear.core.model.vo.mail.TaskReminderMailVo;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import co.jinear.core.service.mail.MailService;
import co.jinear.core.service.notification.NotificationCreateService;
import co.jinear.core.service.task.subscription.TaskSubscriptionListingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskReminderAccountReachOutService {

    private final TaskReminderMailVoMapper taskReminderMailVoMapper;
    private final MailService mailService;
    private final NotificationCreateService notificationCreateService;
    private final TaskReminderNotificationConverter taskReminderNotificationConverter;
    private final TaskSubscriptionListingService taskSubscriptionListingService;

    @Async
    public void notify(TaskDto taskDto, TaskReminderDto taskReminderDto, ReminderJobDto reminderJobDto) {
        List<TaskSubscriptionWithCommunicationPreferencesDto> taskSubscribers = taskSubscriptionListingService.retrieveSubscribersWithCommunicationInfo(taskDto.getTaskId());
        sendNotification(taskSubscribers, taskDto, taskReminderDto);
        sendMail(taskSubscribers, taskDto, taskReminderDto, reminderJobDto);
    }

    private void sendMail(List<TaskSubscriptionWithCommunicationPreferencesDto> taskSubscribers, TaskDto taskDto, TaskReminderDto taskReminderDto, ReminderJobDto reminderJobDto) {
        taskSubscribers
                .stream()
                .filter(subscriber->Boolean.TRUE.equals(subscriber.getHasEmailPermission()))
                .map(acc -> taskReminderMailVoMapper.map(acc.getEmail(), acc.getLocaleType(), acc.getTimeZone(), taskDto, taskReminderDto, reminderJobDto))
                .forEach(this::notifyAccountViaMail);
    }

    private void sendNotification(List<TaskSubscriptionWithCommunicationPreferencesDto> taskSubscribers, TaskDto taskDto, TaskReminderDto taskReminderDto) {
        taskSubscribers
                .stream()
                .map(subscriber -> taskReminderNotificationConverter.convert(subscriber, taskDto, taskReminderDto))
                .forEach(this::notifyAccountViaPushNotification);
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

    private void notifyAccountViaPushNotification(NotificationSendVo notificationSendVo) {
        try {
            log.info("Task reminder notify account with push notif has started. notificationSendVo: {}", notificationSendVo);
            notificationCreateService.create(notificationSendVo);
            log.info("Task reminder notify account with push notif has completed.");
        } catch (Exception e) {
            log.error("Task reminder notify account with push notif has failed. ", e);
        }
    }
}
