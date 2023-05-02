package co.jinear.core.service.notification;

import co.jinear.core.model.dto.notification.NotificationTargetDto;
import co.jinear.core.model.entity.notification.NotificationEvent;
import co.jinear.core.model.enumtype.notification.NotificationEventState;
import co.jinear.core.model.vo.notification.NotificationMessageVo;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import co.jinear.core.service.notification.provider.NotificationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationCreateService {

    private final NotificationTargetRetrieveService notificationTargetRetrieveService;
    private final NotificationEventOperationService notificationEventOperationService;
    private final NotificationClient notificationClient;

    public void create(NotificationSendVo notificationSendVo) {
        log.info("Send notification has started. notificationSendVo: {}", notificationSendVo);
        NotificationEvent notificationEvent = notificationEventOperationService.initialize(notificationSendVo);
        NotificationMessageVo notificationMessageVo = mapToNotificationMessageVo(notificationSendVo);
        fanOutAccountNotificationTargets(notificationMessageVo);
        notificationEventOperationService.updateEventState(notificationEvent, NotificationEventState.SENT);
    }

    private void fanOutAccountNotificationTargets(NotificationMessageVo notificationMessageVo) {
        if (Boolean.FALSE.equals(notificationMessageVo.getIsSilent())) {
            log.info("Fan out account notification targets has started.");
            Optional.of(notificationMessageVo)
                    .map(NotificationMessageVo::getTargetIds)
                    .map(List::isEmpty)
                    .filter(Boolean.FALSE::equals)
                    .ifPresent(hasTargets -> send(notificationMessageVo));
        }
    }

    private void send(NotificationMessageVo notificationMessageVo) {
        log.info("Send notification has started. notificationMessageVo: {}", notificationMessageVo);
        try {
            notificationClient.send(notificationMessageVo);
        } catch (Exception e) {
            log.error("Send notification has failed.", e);
        }
    }

    private NotificationMessageVo mapToNotificationMessageVo(NotificationSendVo notificationSendVo) {
        List<String> targetIds = retrieveTargetIds(notificationSendVo.getAccountId());

        NotificationMessageVo notificationMessageVo = new NotificationMessageVo();
        notificationMessageVo.setAccountId(notificationSendVo.getAccountId());
        notificationMessageVo.setTitle(notificationSendVo.getTitle());
        notificationMessageVo.setText(notificationSendVo.getText());
        notificationMessageVo.setLaunchUrl(notificationSendVo.getLaunchUrl());
        notificationMessageVo.setLocaleType(notificationSendVo.getLocaleType());
        notificationMessageVo.setTargetIds(targetIds);
        notificationMessageVo.setIsSilent(notificationMessageVo.getIsSilent());
        return notificationMessageVo;
    }

    private List<String> retrieveTargetIds(String accountId) {
        return Optional.of(accountId)
                .map(notificationTargetRetrieveService::retrieveLatestAccountTargets)
                .map(Collection::stream)
                .map(notificationTargetDtoStream -> notificationTargetDtoStream.map(NotificationTargetDto::getExternalTargetId))
                .map(Stream::toList)
                .orElse(Collections.emptyList());
    }
}
