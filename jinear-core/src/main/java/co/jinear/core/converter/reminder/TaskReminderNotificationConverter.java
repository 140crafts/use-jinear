package co.jinear.core.converter.reminder;

import co.jinear.core.converter.task.TaskTagConverter;
import co.jinear.core.model.dto.account.AccountCommunicationPermissionDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskReminderDto;
import co.jinear.core.model.enumtype.localestring.LocaleStringType;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import co.jinear.core.service.account.AccountCommunicationPermissionService;
import co.jinear.core.service.mail.LocaleStringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static co.jinear.core.model.enumtype.notification.NotificationTemplateType.TASK_REMINDER;
import static co.jinear.core.service.mail.LocaleStringService.TASK_REMINDER_LOCALE_STRING_MAP;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskReminderNotificationConverter {

    private final LocaleStringService localeStringService;
    private final TaskTagConverter taskTagConverter;
    private final AccountCommunicationPermissionService accountCommunicationPermissionService;

    public NotificationSendVo mapNotificationSendVo(String accountId, LocaleType localeType, TaskDto taskDto, TaskReminderDto taskReminderDto) {
        LocaleStringType taskReminderTypeLocaleString = TASK_REMINDER_LOCALE_STRING_MAP.getOrDefault(taskReminderDto.getTaskReminderType(), LocaleStringType.TASK_REMINDER_TYPE_SPECIFIC_DATE);
        String taskReminderTypeText = localeStringService.retrieveLocalString(taskReminderTypeLocaleString, localeType);
        String taskTag = taskTagConverter.retrieveTaskTag(taskDto);
        boolean isSilent = hasPushNotificationPermission(accountId);

        NotificationSendVo notificationSendVo = new NotificationSendVo();
        notificationSendVo.setTemplateType(TASK_REMINDER);
        notificationSendVo.setLocaleType(localeType);
        notificationSendVo.setAccountId(accountId);
        notificationSendVo.setWorkspaceId(taskDto.getWorkspaceId());
        notificationSendVo.setTeamId(taskDto.getTeamId());
        notificationSendVo.setIsSilent(isSilent);
        notificationSendVo.setParams(Map.of("taskTag", taskTag, "taskTitle", taskDto.getTitle(), "reminderTypeText", taskReminderTypeText));

        return notificationSendVo;
    }

    private boolean hasPushNotificationPermission(String accountId) {
        AccountCommunicationPermissionDto accountCommunicationPermissionDto = accountCommunicationPermissionService.retrieve(accountId);
        return Boolean.TRUE.equals(accountCommunicationPermissionDto.getPushNotification());
    }
}
