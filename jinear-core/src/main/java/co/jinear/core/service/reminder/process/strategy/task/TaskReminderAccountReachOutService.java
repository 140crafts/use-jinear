package co.jinear.core.service.reminder.process.strategy.task;

import co.jinear.core.converter.reminder.TaskReminderMailVoMapper;
import co.jinear.core.converter.reminder.TaskReminderNotificationConverter;
import co.jinear.core.model.dto.account.AccountCommunicationPermissionDto;
import co.jinear.core.model.dto.account.AccountDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import co.jinear.core.model.dto.reminder.ReminderJobDto;
import co.jinear.core.model.dto.task.DetailedTaskSubscriptionDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskReminderDto;
import co.jinear.core.model.vo.mail.TaskReminderMailVo;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import co.jinear.core.service.account.AccountCommunicationPermissionService;
import co.jinear.core.service.mail.MailService;
import co.jinear.core.service.notification.NotificationCreateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskReminderAccountReachOutService {

    private final TaskReminderMailVoMapper taskReminderMailVoMapper;
    private final MailService mailService;
    private final NotificationCreateService notificationCreateService;
    private final TaskReminderNotificationConverter taskReminderNotificationConverter;
    private final AccountCommunicationPermissionService accountCommunicationPermissionService;

    @Async
    public void notify(List<DetailedTaskSubscriptionDto> taskSubscribers, TaskDto taskDto, TaskReminderDto taskReminderDto, ReminderJobDto reminderJobDto) {
        sendNotification(taskSubscribers, taskDto, taskReminderDto);
        sendMail(taskSubscribers, taskDto, taskReminderDto, reminderJobDto);
    }

    private void sendMail(List<DetailedTaskSubscriptionDto> taskSubscribers, TaskDto taskDto, TaskReminderDto taskReminderDto, ReminderJobDto reminderJobDto) {
        Set<TaskReminderMailVo> receiverSet = new HashSet<>();

        taskSubscribers
                .stream()
                .map(DetailedTaskSubscriptionDto::getAccountDto)
                .filter(this::isEmailCommunicationPermitted)
                .map(acc -> taskReminderMailVoMapper.map(acc.getEmail(), acc.getLocaleType(), acc.getTimeZone(), taskDto, taskReminderDto, reminderJobDto))
                .forEach(receiverSet::add);

        //todo remove this after subscription module complete
        Optional.of(taskDto)
                .map(TaskDto::getOwner)
                .filter(this::hasEmailPermission)
                .map(acc -> taskReminderMailVoMapper.map(acc.getEmail(), acc.getLocaleType(), acc.getTimeZone(), taskDto, taskReminderDto, reminderJobDto))
                .ifPresent(receiverSet::add);

        //todo remove this after subscription module complete
        Optional.of(taskDto)
                .map(TaskDto::getAssignedToAccount)
                .filter(this::hasEmailPermission)
                .map(acc -> taskReminderMailVoMapper.map(acc.getEmail(), acc.getLocaleType(), acc.getTimeZone(), taskDto, taskReminderDto, reminderJobDto))
                .ifPresent(receiverSet::add);

        receiverSet.forEach(this::notifyAccountViaMail);
    }

    private void sendNotification(List<DetailedTaskSubscriptionDto> taskSubscribers, TaskDto taskDto, TaskReminderDto taskReminderDto) {
        Set<NotificationSendVo> receiverSet = new HashSet<>();

        taskSubscribers
                .stream()
                .map(DetailedTaskSubscriptionDto::getAccountDto)
                .map(plainAccountProfileDto -> taskReminderNotificationConverter.mapNotificationSendVo(plainAccountProfileDto.getAccountId(), plainAccountProfileDto.getLocaleType(), taskDto, taskReminderDto))
                .forEach(receiverSet::add);

        //todo remove this after subscription module complete
        Optional.of(taskDto)
                .map(TaskDto::getOwner)
                .map(acc -> taskReminderNotificationConverter.mapNotificationSendVo(acc.getAccountId(), acc.getLocaleType(), taskDto, taskReminderDto))
                .ifPresent(receiverSet::add);

        //todo remove this after subscription module complete
        Optional.of(taskDto)
                .map(TaskDto::getAssignedToAccount)
                .map(acc -> taskReminderNotificationConverter.mapNotificationSendVo(acc.getAccountId(), acc.getLocaleType(), taskDto, taskReminderDto))
                .ifPresent(receiverSet::add);

        receiverSet.forEach(this::notifyAccountViaPushNotification);
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

    private boolean isEmailCommunicationPermitted(AccountDto accountDto) {
        return Optional.of(accountDto)
                .map(AccountDto::getAccountId)
                .map(accountCommunicationPermissionService::retrieve)
                .map(AccountCommunicationPermissionDto::getEmail)
                .orElse(Boolean.FALSE);
    }

    private boolean hasEmailPermission(PlainAccountProfileDto account) {
        AccountCommunicationPermissionDto accountCommunicationPermissionDto = accountCommunicationPermissionService.retrieve(account.getAccountId());
        return Boolean.TRUE.equals(accountCommunicationPermissionDto.getEmail());
    }
}
