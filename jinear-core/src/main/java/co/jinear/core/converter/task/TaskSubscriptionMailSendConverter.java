package co.jinear.core.converter.task;

import co.jinear.core.config.properties.FeProperties;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskSubscriptionWithCommunicationPreferencesDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.enumtype.localestring.LocaleStringType;
import co.jinear.core.model.enumtype.workspace.WorkspaceActivityType;
import co.jinear.core.model.vo.mail.GenericInfoWithSubInfoMailWithCtaButtonVo;
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
import static co.jinear.core.system.NormalizeHelper.SPACE_STRING;
import static java.util.Map.entry;

@Component
@RequiredArgsConstructor
public class TaskSubscriptionMailSendConverter {

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
            entry(CHECKLIST_ITEM_INITIALIZED, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_CHECKLIST_ITEM_INITIALIZED),
            entry(TASK_NEW_COMMENT, WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_NEW_COMMENT)
    );

    private static final String MAIL_BODY_TITLE = "${notificationTitle}";
    private static final String MAIL_BODY_TEXT = "[${taskTag}] ${taskTitle}";

    public GenericInfoWithSubInfoMailWithCtaButtonVo mapToGenericMailWithSubInfoMailVo(TaskSubscriptionWithCommunicationPreferencesDto subscription,
                                                                                       NotifyTaskSubscribersVo notifyTaskSubscribersVo) {
        String taskTag = taskTagConverter.retrieveTaskTag(notifyTaskSubscribersVo.getTaskDto());
        String mailBodyTitle = retrieveMailBodyTitle(subscription, notifyTaskSubscribersVo);
        String mailBodyText = retrieveMailBodyText(notifyTaskSubscribersVo, taskTag);
        String mailTitle = mailBodyText + SPACE_STRING + mailBodyTitle;
        String subText = retrieveSubtext(subscription);
        String ctaLabel = retrieveCtaLabel(subscription);
        String href = retrieveTaskUrl(notifyTaskSubscribersVo.getTaskDto(), taskTag);

        GenericInfoWithSubInfoMailWithCtaButtonVo vo = new GenericInfoWithSubInfoMailWithCtaButtonVo();
        vo.setEmail(subscription.getEmail());
        vo.setPreferredLocale(subscription.getLocaleType());
        vo.setMailTitle(mailTitle);
        vo.setMailBodyTitle(mailBodyTitle);
        vo.setMailBodyText(mailBodyText);
        vo.setMailBodySubtext(subText);
        vo.setHref(href);
        vo.setCtaLabel(ctaLabel);
        return vo;
    }

    private String retrieveMailBodyTitle(TaskSubscriptionWithCommunicationPreferencesDto subscription, NotifyTaskSubscribersVo notifyTaskSubscribersVo) {
        LocaleStringType titleLocaleStringType = TITLE_MAP.getOrDefault(notifyTaskSubscribersVo.getWorkspaceActivityType(), WORKSPACE_ACTIVITY_NOTIFICATION_TITLE_GENERIC_ACTIVITY);
        String notificationTitle = localeStringService.retrieveLocalString(titleLocaleStringType, subscription.getLocaleType());

        StringSubstitutor sub = new StringSubstitutor(Map.of("notificationTitle", notificationTitle));
        return sub.replace(MAIL_BODY_TITLE);
    }

    private String retrieveMailBodyText(NotifyTaskSubscribersVo notifyTaskSubscribersVo, String taskTag) {
        TaskDto taskDto = notifyTaskSubscribersVo.getTaskDto();
        String taskTitle = taskDto.getTitle();

        StringSubstitutor sub = new StringSubstitutor(Map.of("taskTag", taskTag, "taskTitle", taskTitle));
        return sub.replace(MAIL_BODY_TEXT);
    }

    private String retrieveSubtext(TaskSubscriptionWithCommunicationPreferencesDto subscription) {
        return localeStringService.retrieveLocalString(WORKSPACE_ACTIVITY_NOTIFICATION_SUBTEXT_VISIT_TASK_PAGE_TO_GET_MORE_DETAIL, subscription.getLocaleType());
    }

    private String retrieveCtaLabel(TaskSubscriptionWithCommunicationPreferencesDto subscription) {
        return localeStringService.retrieveLocalString(WORKSPACE_ACTIVITY_NOTIFICATION_CTA_LABEL, subscription.getLocaleType());
    }

    private String retrieveTaskUrl(TaskDto taskDto, String taskTag) {
        String taskUrl = feProperties.getTaskUrl();
        String workspaceUsername = Optional.of(taskDto).map(TaskDto::getWorkspace).map(WorkspaceDto::getUsername).orElse(EMPTY_STRING);
        return taskUrl.replaceAll(Pattern.quote("{workspaceName}"), workspaceUsername)
                .replaceAll(Pattern.quote("{taskTag}"), taskTag);
    }
}
