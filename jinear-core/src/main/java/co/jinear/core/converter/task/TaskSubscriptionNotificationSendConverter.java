package co.jinear.core.converter.task;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskSubscriptionWithCommunicationPreferencesDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.enumtype.localestring.LocaleStringType;
import co.jinear.core.model.enumtype.notification.NotificationType;
import co.jinear.core.model.enumtype.workspace.WorkspaceActivityType;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import co.jinear.core.model.vo.task.NotifyTaskSubscribersVo;
import co.jinear.core.service.mail.LocaleStringService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static co.jinear.core.model.enumtype.localestring.LocaleStringType.*;
import static co.jinear.core.model.enumtype.workspace.WorkspaceActivityType.*;
import static co.jinear.core.system.NormalizeHelper.EMPTY_STRING;
import static java.util.Map.entry;

@Component
@RequiredArgsConstructor
public class TaskSubscriptionNotificationSendConverter {

    private final LocaleStringService localeStringService;
    private final TaskTagConverter taskTagConverter;
    private final FeProperties feProperties;

    public static final Map<WorkspaceActivityType, LocaleStringType> TITLE_MAP = Map.ofEntries(
            entry(TASK_INITIALIZED, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_INITIALIZED),
            entry(TASK_CLOSED, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_CLOSED),
            entry(EDIT_TASK_TITLE, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_EDIT_TASK_TITLE),
            entry(EDIT_TASK_DESC, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_EDIT_TASK_DESC),
            entry(TASK_UPDATE_TOPIC, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_UPDATE_TOPIC),
            entry(TASK_UPDATE_WORKFLOW_STATUS, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_UPDATE_WORKFLOW_STATUS),
            entry(TASK_CHANGE_ASSIGNEE, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_CHANGE_ASSIGNEE),
            entry(TASK_CHANGE_ASSIGNED_DATE, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_CHANGE_ASSIGNED_DATE),
            entry(TASK_CHANGE_DUE_DATE, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_TASK_CHANGE_DUE_DATE),
            entry(RELATION_INITIALIZED, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_RELATION_INITIALIZED),
            entry(RELATION_REMOVED, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_RELATION_REMOVED)
    );
    private static final String NOTIFICATION_TEXT = "[${taskTag}] ${taskTitle}";

    public NotificationSendVo mapToNotificationSendVo(TaskSubscriptionWithCommunicationPreferencesDto subscription,
                                                      NotifyTaskSubscribersVo notifyTaskSubscribersVo) {
        TaskDto taskDto = notifyTaskSubscribersVo.getTaskDto();
        String taskTag = taskTagConverter.retrieveTaskTag(taskDto);
        String title = retrieveNotificationTitle(subscription, notifyTaskSubscribersVo);
        String text = retrieveNotificationText(taskDto, taskTag);
        String launchUrl = retrieveTaskUrl(taskDto, taskTag);
        Boolean isSilent = Boolean.FALSE.equals(subscription.getHasPushNotificationPermission());

        NotificationSendVo notificationSendVo = new NotificationSendVo();
        notificationSendVo.setAccountId(subscription.getAccountId());
        notificationSendVo.setWorkspaceId(taskDto.getWorkspaceId());
        notificationSendVo.setTeamId(taskDto.getTeamId());
        notificationSendVo.setTitle(title);
        notificationSendVo.setText(text);
        notificationSendVo.setLaunchUrl(launchUrl);
        notificationSendVo.setLocaleType(subscription.getLocaleType());
        notificationSendVo.setIsSilent(isSilent);
        notificationSendVo.setNotificationType(NotificationType.WORKSPACE_ACTIVITY);
        return notificationSendVo;
    }

    private String retrieveNotificationTitle(TaskSubscriptionWithCommunicationPreferencesDto subscription, NotifyTaskSubscribersVo notifyTaskSubscribersVo) {
        LocaleStringType titleLocaleStringType = TITLE_MAP.get(notifyTaskSubscribersVo.getWorkspaceActivityType());
        return localeStringService.retrieveLocalString(titleLocaleStringType, subscription.getLocaleType());
    }

    private String retrieveNotificationText(TaskDto taskDto, String taskTag) {
        String taskTitle = taskDto.getTitle();
        StringSubstitutor sub = new StringSubstitutor(Map.of("taskTag", taskTag, "taskTitle", taskTitle));
        return sub.replace(NOTIFICATION_TEXT);
    }

    private String retrieveTaskUrl(TaskDto taskDto, String taskTag) {
        String taskUrl = feProperties.getTaskUrl();
        String workspaceUsername = Optional.of(taskDto).map(TaskDto::getWorkspace).map(WorkspaceDto::getUsername).orElse(EMPTY_STRING);
        return taskUrl.replaceAll(Pattern.quote("{workspaceName}"), workspaceUsername)
                .replaceAll(Pattern.quote("{taskTag}"), taskTag);
    }
}
