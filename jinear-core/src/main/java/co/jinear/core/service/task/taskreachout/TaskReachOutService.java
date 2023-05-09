package co.jinear.core.service.task.taskreachout;

import co.jinear.core.converter.task.TaskSubscriptionMailSendConverter;
import co.jinear.core.converter.task.TaskSubscriptionNotificationSendConverter;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.task.TaskSubscriptionWithCommunicationPreferencesDto;
import co.jinear.core.model.dto.workspace.WorkspaceDto;
import co.jinear.core.model.vo.mail.GenericInfoWithSubInfoMailWithCtaButtonVo;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import co.jinear.core.model.vo.task.NotifyTaskSubscribersVo;
import co.jinear.core.service.mail.MailService;
import co.jinear.core.service.notification.NotificationCreateService;
import co.jinear.core.service.task.subscription.TaskSubscriptionListingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskReachOutService {

    private final TaskSubscriptionListingService taskSubscriptionListingService;
    private final NotificationCreateService notificationCreateService;
    private final MailService mailService;
    private final TaskSubscriptionNotificationSendConverter taskSubscriptionNotificationSendConverter;
    private final TaskSubscriptionMailSendConverter taskSubscriptionMailSendConverter;

    public void notifyTaskSubscribers(NotifyTaskSubscribersVo notifyTaskSubscribersVo) {
        if (checkRelatedWorkspaceIsNotPersonal(notifyTaskSubscribersVo)) {
            log.info("Notify task subscribers has started. notifyTaskSubscribersVo: {}", notifyTaskSubscribersVo);
            List<TaskSubscriptionWithCommunicationPreferencesDto> taskSubscribers = taskSubscriptionListingService.retrieveSubscribersWithCommunicationInfo(notifyTaskSubscribersVo.getTaskDto().getTaskId());
            mapAndCreatePushNotification(taskSubscribers, notifyTaskSubscribersVo);
            mapAndCreateGenericMail(notifyTaskSubscribersVo, taskSubscribers);
        }
    }

    private void mapAndCreatePushNotification(List<TaskSubscriptionWithCommunicationPreferencesDto> taskSubscribers, NotifyTaskSubscribersVo notifyTaskSubscribersVo) {
        taskSubscribers.stream()
                .map(subscription -> taskSubscriptionNotificationSendConverter.mapToNotificationSendVo(subscription, notifyTaskSubscribersVo))
                .forEach(this::createPushNotification);
    }

    private void createPushNotification(NotificationSendVo notificationSendVo) {
        try {
            log.info("Task activity, reach out via push notification has started. notificationSendVo: {}", notificationSendVo);
            notificationCreateService.create(notificationSendVo);
            log.info("Task activity, reach out via push notification has completed.");
        } catch (Exception e) {
            log.error("Task activity, reach out via push notification has failed. ", e);
        }
    }

    private void mapAndCreateGenericMail(NotifyTaskSubscribersVo notifyTaskSubscribersVo, List<TaskSubscriptionWithCommunicationPreferencesDto> taskSubscribers) {
        taskSubscribers.stream()
                .map(subscription -> taskSubscriptionMailSendConverter.mapToGenericMailWithSubInfoMailVo(subscription, notifyTaskSubscribersVo))
                .forEach(this::createMail);
    }

    private void createMail(GenericInfoWithSubInfoMailWithCtaButtonVo genericInfoWithSubInfoMailWithCtaButtonVo) {
        try {
            log.info("Task activity, reach out via email has started. genericInfoWithSubInfoMailWithCtaButtonVo: {}", genericInfoWithSubInfoMailWithCtaButtonVo);
            mailService.sendGenericInfoWithSubInfoWithCtaButtonMail(genericInfoWithSubInfoMailWithCtaButtonVo);
            log.info("Task activity, reach out via email has completed.");
        } catch (Exception e) {
            log.error("Task activity, reach out via email has failed. ", e);
        }
    }

    private static boolean checkRelatedWorkspaceIsNotPersonal(NotifyTaskSubscribersVo notifyTaskSubscribersVo) {
        boolean isPersonal = Optional.of(notifyTaskSubscribersVo)
                .map(NotifyTaskSubscribersVo::getTaskDto)
                .map(TaskDto::getWorkspace)
                .map(WorkspaceDto::getIsPersonal)
                .orElse(Boolean.FALSE);
        return !isPersonal;
    }
}
