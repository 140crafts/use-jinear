package co.jinear.core.converter.reminder;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.converter.task.TaskTagConverter;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskReminderDto;
import co.jinear.core.model.dto.task.TaskSubscriptionWithCommunicationPreferencesDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.enumtype.localestring.LocaleStringType;
import co.jinear.core.model.enumtype.notification.NotificationType;
import co.jinear.core.model.enumtype.task.TaskReminderType;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import co.jinear.core.service.mail.LocaleStringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static co.jinear.core.system.NormalizeHelper.EMPTY_STRING;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskReminderNotificationConverter {

    private static final String NOTIFICATION_TITLE = "[${taskTag}] ${taskTitle}";
    private static final String NOTIFICATION_TEXT = "${reminderTypeText}";
    public static final Map<TaskReminderType, LocaleStringType> TASK_REMINDER_LOCALE_STRING_MAP =
            Map.of(
                    TaskReminderType.ASSIGNED_DATE, LocaleStringType.TASK_REMINDER_TYPE_ASSIGNED_DATE,
                    TaskReminderType.DUE_DATE, LocaleStringType.TASK_REMINDER_TYPE_DUE_DATE,
                    TaskReminderType.SPECIFIC_DATE, LocaleStringType.TASK_REMINDER_TYPE_SPECIFIC_DATE
            );

    private final LocaleStringService localeStringService;
    private final TaskTagConverter taskTagConverter;
    private final FeProperties feProperties;

    public NotificationSendVo convert(TaskSubscriptionWithCommunicationPreferencesDto taskSubscriptionWithCommunicationPreferencesDto, TaskDto taskDto, TaskReminderDto taskReminderDto) {
        LocaleStringType taskReminderTypeLocaleString = TASK_REMINDER_LOCALE_STRING_MAP.getOrDefault(taskReminderDto.getTaskReminderType(), LocaleStringType.TASK_REMINDER_TYPE_SPECIFIC_DATE);
        String taskReminderTypeText = localeStringService.retrieveLocalString(taskReminderTypeLocaleString, taskSubscriptionWithCommunicationPreferencesDto.getLocaleType());
        String taskTag = taskTagConverter.retrieveTaskTag(taskDto);
        String launchUrl = retrieveTaskUrl(taskDto, taskTag);
        boolean isSilent = Boolean.FALSE.equals(taskSubscriptionWithCommunicationPreferencesDto.getHasPushNotificationPermission());
        return mapValues(taskDto, taskSubscriptionWithCommunicationPreferencesDto, taskReminderTypeText, taskTag, isSilent, launchUrl);
    }

    private NotificationSendVo mapValues(TaskDto taskDto, TaskSubscriptionWithCommunicationPreferencesDto taskSubscriptionWithCommunicationPreferencesDto, String taskReminderTypeText, String taskTag, boolean isSilent, String launchUrl) {
        NotificationSendVo notificationSendVo = new NotificationSendVo();

        notificationSendVo.setAccountId(taskSubscriptionWithCommunicationPreferencesDto.getAccountId());
        notificationSendVo.setWorkspaceId(taskDto.getWorkspaceId());
        notificationSendVo.setTeamId(taskDto.getTeamId());
        notificationSendVo.setTaskId(taskDto.getTaskId());
        notificationSendVo.setTaskTag(taskTag);
        notificationSendVo.setLocaleType(taskSubscriptionWithCommunicationPreferencesDto.getLocaleType());

        notificationSendVo.setTitle(retrieveTitle(taskTag, taskDto.getTitle()));
        notificationSendVo.setText(retrieveText(taskReminderTypeText));
        notificationSendVo.setLaunchUrl(launchUrl);

        notificationSendVo.setIsSilent(isSilent);
        notificationSendVo.setNotificationType(NotificationType.TASK_REMINDER);
        notificationSendVo.setRelatedTaskTag(taskTag);
        return notificationSendVo;
    }

    private String retrieveTitle(String taskTag, String taskTitle) {
        StringSubstitutor sub = new StringSubstitutor(Map.of("taskTag", taskTag, "taskTitle", taskTitle));
        return sub.replace(NOTIFICATION_TITLE);
    }

    private String retrieveText(String taskReminderTypeText) {
        StringSubstitutor sub = new StringSubstitutor(Map.of("reminderTypeText", taskReminderTypeText));
        return sub.replace(NOTIFICATION_TEXT);
    }

    private String retrieveTaskUrl(TaskDto taskDto, String taskTag) {
        String taskUrl = feProperties.getTaskUrl();
        String workspaceUsername = Optional.of(taskDto).map(TaskDto::getWorkspace).map(WorkspaceDto::getUsername).orElse(EMPTY_STRING);
        return taskUrl.replaceAll(Pattern.quote("{workspaceName}"), workspaceUsername)
                .replaceAll(Pattern.quote("{taskTag}"), taskTag);
    }
}
