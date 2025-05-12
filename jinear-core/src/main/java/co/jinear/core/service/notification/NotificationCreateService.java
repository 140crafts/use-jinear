package co.jinear.core.service.notification;

import co.jinear.core.model.dto.notification.NotificationMessageExternalDataDto;
import co.jinear.core.model.dto.notification.NotificationTargetDto;
import co.jinear.core.model.entity.notification.NotificationEvent;
import co.jinear.core.model.enumtype.notification.NotificationEventState;
import co.jinear.core.model.vo.notification.NotificationMessageVo;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import co.jinear.core.service.notification.provider.NotificationDeliveryStrategy;
import co.jinear.core.service.notification.provider.NotificationDeliveryStrategyFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationCreateService {

    private final NotificationTargetRetrieveService notificationTargetRetrieveService;
    private final NotificationEventOperationService notificationEventOperationService;
    private final NotificationDeliveryStrategyFactory notificationDeliveryStrategyFactory;

    public void create(NotificationSendVo notificationSendVo) {
        log.info("Send notification has started. notificationSendVo: {}", notificationSendVo);
        NotificationEvent notificationEvent = notificationEventOperationService.initialize(notificationSendVo);
        List<NotificationMessageVo> notificationMessages = mapToNotificationMessageVo(notificationSendVo);
        notificationMessages.forEach(this::send);
        notificationEventOperationService.updateEventState(notificationEvent, NotificationEventState.SENT);
    }

    private void send(NotificationMessageVo notificationMessageVo) {
        log.info("Send notification has started. notificationMessageVo: {}", notificationMessageVo);
        if (Boolean.FALSE.equals(notificationMessageVo.getIsSilent())) {
            trySendNotification(notificationMessageVo);
        }
    }

    private void trySendNotification(NotificationMessageVo notificationMessageVo) {
        try {
            NotificationDeliveryStrategy notificationDeliveryStrategy = notificationDeliveryStrategyFactory.getStrategy(notificationMessageVo.getTarget().getProviderType());
            notificationDeliveryStrategy.send(notificationMessageVo);
        } catch (Exception e) {
            log.error("Send notification has failed.", e);
        }
    }

    private List<NotificationMessageVo> mapToNotificationMessageVo(NotificationSendVo notificationSendVo) {
        List<NotificationTargetDto> targets = retrieveTargets(notificationSendVo.getAccountId());
        return targets.stream()
                .map(target -> mapForTarget(notificationSendVo, target))
                .toList();
    }

    @NonNull
    private static NotificationMessageVo mapForTarget(NotificationSendVo notificationSendVo, NotificationTargetDto target) {
        NotificationMessageExternalDataDto data = new NotificationMessageExternalDataDto();
        data.setWorkspaceId(notificationSendVo.getWorkspaceId());
        data.setTeamId(notificationSendVo.getTeamId());
        data.setTaskId(notificationSendVo.getTaskId());
        data.setTaskTag(notificationSendVo.getTaskTag());
        data.setNotificationType(notificationSendVo.getNotificationType());
        data.setSenderSessionId(notificationSendVo.getSenderSessionId());

        NotificationMessageVo notificationMessageVo = new NotificationMessageVo();
        notificationMessageVo.setAccountId(notificationSendVo.getAccountId());
        notificationMessageVo.setTitle(notificationSendVo.getTitle());
        notificationMessageVo.setText(notificationSendVo.getText());
        notificationMessageVo.setLaunchUrl(notificationSendVo.getLaunchUrl());
        notificationMessageVo.setLocaleType(notificationSendVo.getLocaleType());
        notificationMessageVo.setTarget(target);
        notificationMessageVo.setIsSilent(notificationSendVo.getIsSilent());
        notificationMessageVo.setData(data);
        return notificationMessageVo;
    }

    private List<NotificationTargetDto> retrieveTargets(String accountId) {
        return Optional.of(accountId)
                .map(notificationTargetRetrieveService::retrieveLatestAccountTargets)
                .orElse(Collections.emptyList());
    }
}
