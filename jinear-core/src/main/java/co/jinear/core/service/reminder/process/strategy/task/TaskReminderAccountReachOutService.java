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
import co.jinear.core.model.entity.account.AccountCommunicationPermission;
import co.jinear.core.model.vo.mail.TaskReminderMailVo;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import co.jinear.core.service.account.AccountCommunicationPermissionService;
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

    private final TaskReminderMailVoMapper taskReminderMailVoMapper;
    private final MailService mailService;
    private final NotificationFireService notificationFireService;
    private final TaskReminderNotificationConverter taskReminderNotificationConverter;
    private final AccountCommunicationPermissionService accountCommunicationPermissionService;

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
                .filter(this::hasPushNotificationPermission)
                .map(acc -> taskReminderNotificationConverter.mapNotificationSendVo(acc.getAccountId(), acc.getLocaleType(), taskDto, taskReminderDto))
                .ifPresent(receiverSet::add);

        //todo remove this after subscription module complete
        Optional.of(taskDto)
                .map(TaskDto::getAssignedToAccount)
                .filter(this::hasPushNotificationPermission)
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

    private boolean isEmailCommunicationPermitted(AccountDto accountDto) {
        return Optional.of(accountDto)
                .map(AccountDto::getCommunicationPermission)
                .map(AccountCommunicationPermission::getEmail)
                .orElse(Boolean.FALSE);
    }

    private boolean hasEmailPermission(PlainAccountProfileDto account) {
        AccountCommunicationPermissionDto accountCommunicationPermissionDto = accountCommunicationPermissionService.retrieve(account.getAccountId());
        return Boolean.TRUE.equals(accountCommunicationPermissionDto.getEmail());
    }

    private boolean hasPushNotificationPermission(PlainAccountProfileDto account) {
        AccountCommunicationPermissionDto accountCommunicationPermissionDto = accountCommunicationPermissionService.retrieve(account.getAccountId());
        return Boolean.TRUE.equals(accountCommunicationPermissionDto.getPushNotification());
    }
}
