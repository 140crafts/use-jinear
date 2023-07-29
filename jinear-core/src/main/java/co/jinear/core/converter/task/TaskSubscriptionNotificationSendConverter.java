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
            entry(RELATION_REMOVED, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_RELATION_REMOVED),
            entry(CHECKLIST_INITIALIZED, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_INITIALIZED),
            entry(CHECKLIST_REMOVED, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_REMOVED),
            entry(CHECKLIST_TITLE_CHANGED, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_TITLE_CHANGED),
            entry(CHECKLIST_ITEM_CHECKED_STATUS_CHANGED, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_ITEM_CHECKED_STATUS_CHANGED),
            entry(CHECKLIST_ITEM_LABEL_CHANGED, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_ITEM_LABEL_CHANGED),
            entry(CHECKLIST_ITEM_REMOVED, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_ITEM_REMOVED),
            entry(CHECKLIST_ITEM_INITIALIZED, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_ITEM_INITIALIZED)
    );

    public static final Map<WorkspaceActivityType, NotificationType> NOTIFICATION_TYPE_MAP = Map.ofEntries(
            entry(TASK_INITIALIZED, NotificationType.TASK_INITIALIZED),
            entry(TASK_CLOSED, NotificationType.TASK_CLOSED),
            entry(EDIT_TASK_TITLE, NotificationType.EDIT_TASK_TITLE),
            entry(EDIT_TASK_DESC, NotificationType.EDIT_TASK_DESC),
            entry(TASK_UPDATE_TOPIC, NotificationType.TASK_UPDATE_TOPIC),
            entry(TASK_UPDATE_WORKFLOW_STATUS, NotificationType.TASK_UPDATE_WORKFLOW_STATUS),
            entry(TASK_CHANGE_ASSIGNEE, NotificationType.TASK_CHANGE_ASSIGNEE),
            entry(TASK_CHANGE_ASSIGNED_DATE, NotificationType.TASK_CHANGE_ASSIGNED_DATE),
            entry(TASK_CHANGE_DUE_DATE, NotificationType.TASK_CHANGE_DUE_DATE),
            entry(RELATION_INITIALIZED, NotificationType.RELATION_INITIALIZED),
            entry(RELATION_REMOVED, NotificationType.RELATION_REMOVED),
            entry(CHECKLIST_INITIALIZED, NotificationType.CHECKLIST_INITIALIZED),
            entry(CHECKLIST_REMOVED, NotificationType.CHECKLIST_REMOVED),
            entry(CHECKLIST_TITLE_CHANGED, NotificationType.CHECKLIST_TITLE_CHANGED),
            entry(CHECKLIST_ITEM_CHECKED_STATUS_CHANGED, NotificationType.CHECKLIST_ITEM_CHECKED_STATUS_CHANGED),
            entry(CHECKLIST_ITEM_LABEL_CHANGED, NotificationType.CHECKLIST_ITEM_LABEL_CHANGED),
            entry(CHECKLIST_ITEM_REMOVED, NotificationType.CHECKLIST_ITEM_REMOVED),
            entry(CHECKLIST_ITEM_INITIALIZED, NotificationType.CHECKLIST_ITEM_INITIALIZED)
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
        NotificationType type = NOTIFICATION_TYPE_MAP.getOrDefault(notifyTaskSubscribersVo.getWorkspaceActivityType(), NotificationType.WORKSPACE_ACTIVITY);

        NotificationSendVo notificationSendVo = new NotificationSendVo();
        notificationSendVo.setAccountId(subscription.getAccountId());
        notificationSendVo.setWorkspaceId(taskDto.getWorkspaceId());
        notificationSendVo.setTeamId(taskDto.getTeamId());
        notificationSendVo.setTaskId(taskDto.getTaskId());
        notificationSendVo.setTaskTag(taskTag);
        notificationSendVo.setTitle(title);
        notificationSendVo.setText(text);
        notificationSendVo.setLaunchUrl(launchUrl);
        notificationSendVo.setLocaleType(subscription.getLocaleType());
        notificationSendVo.setIsSilent(isSilent);
        notificationSendVo.setNotificationType(type);
        notificationSendVo.setRelatedTaskTag(taskTag);
        notificationSendVo.setSenderSessionId(notifyTaskSubscribersVo.getPerformingAccountSessionId());
        return notificationSendVo;
    }

    private String retrieveNotificationTitle(TaskSubscriptionWithCommunicationPreferencesDto subscription, NotifyTaskSubscribersVo notifyTaskSubscribersVo) {
        LocaleStringType titleLocaleStringType = TITLE_MAP.getOrDefault(notifyTaskSubscribersVo.getWorkspaceActivityType(), WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_GENERIC_ACTIVITY);
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
