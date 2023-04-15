package co.jinear.core.service.notification;

import co.jinear.core.model.dto.notification.NotificationTargetDto;
import co.jinear.core.model.dto.notification.NotificationTemplateDto;
import co.jinear.core.model.entity.notification.NotificationEvent;
import co.jinear.core.model.enumtype.notification.NotificationEventState;
import co.jinear.core.model.vo.notification.NotificationMessageVo;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import co.jinear.core.service.notification.provider.NotificationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationFireService {

    private final NotificationTargetRetrieveService notificationTargetRetrieveService;
    private final NotificationTemplatePopulateService notificationTemplatePopulateService;
    private final NotificationEventOperationService notificationEventOperationService;
    private final NotificationTemplateRetrieveService templateRetrieveService;
    private final NotificationClient notificationClient;

    public void fire(NotificationSendVo notificationSendVo) {
        log.info("Send notification has started. notificationSendVo: {}", notificationSendVo);
        NotificationTemplateDto notificationTemplateDto = templateRetrieveService.retrieve(notificationSendVo.getTemplateType(), notificationSendVo.getLocaleType());
        NotificationMessageVo notificationMessageVo = notificationTemplatePopulateService.populate(notificationTemplateDto, notificationSendVo);
        NotificationEvent notificationEvent = notificationEventOperationService.initialize(notificationTemplateDto, notificationSendVo);
        fanOutAccountNotificationTargets(notificationSendVo, notificationMessageVo);
        notificationEventOperationService.updateEventState(notificationEvent, NotificationEventState.SENT);
    }

    private void fanOutAccountNotificationTargets(NotificationSendVo notificationSendVo, NotificationMessageVo notificationMessageVo) {
        Optional.of(notificationSendVo)
                .map(NotificationSendVo::getAccountId)
                .map(notificationTargetRetrieveService::retrieveLatestAccountTargets)
                .map(Collection::stream)
                .map(notificationTargetDtoStream -> notificationTargetDtoStream.map(NotificationTargetDto::getExternalTargetId))
                .map(Stream::toList)
                .ifPresent(notificationMessageVo::setTargetIds);

        Optional.of(notificationMessageVo)
                .map(NotificationMessageVo::getTargetIds)
                .map(List::isEmpty)
                .filter(Boolean.FALSE::equals)
                .ifPresent(hasTargets -> send(notificationMessageVo));
    }

    private void send(NotificationMessageVo notificationMessageVo) {
        log.info("Send notification has started. notificationMessageVo: {}", notificationMessageVo);
        try {
            notificationClient.send(notificationMessageVo);
        } catch (Exception e) {
            log.error("Send notification has failed.", e);
        }
    }
}
