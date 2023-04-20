package co.jinear.core.service.notification;

import co.jinear.core.model.dto.notification.NotificationTemplateDto;
import co.jinear.core.model.entity.notification.NotificationEvent;
import co.jinear.core.model.enumtype.notification.NotificationEventState;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import co.jinear.core.repository.NotificationEventRepository;
import co.jinear.core.system.util.DateHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventOperationService {

    private final NotificationEventRepository notificationEventRepository;
    private final NotificationEventParamOperationService notificationEventParamOperationService;

    @Transactional
    public NotificationEvent initialize(NotificationTemplateDto notificationTemplateDto, NotificationSendVo notificationSendVo) {
        log.info("Notification event initialize has started.");
        NotificationEvent notificationEvent = saveInitialEntry(notificationTemplateDto, notificationSendVo);
        log.info("Notification initialized. notificationEventId: {}", notificationEvent.getNotificationEventId());
        notificationEventParamOperationService.initializeEventParams(notificationEvent.getNotificationEventId(), notificationSendVo.getParams());
        return notificationEvent;
    }

    public void updateEventState(NotificationEvent notificationEvent, NotificationEventState eventState) {
        log.info("Update event state has started. notificationEventId: {}, eventState: {}", notificationEvent.getNotificationEventId(), eventState);
        notificationEvent.setEventState(eventState);
        notificationEventRepository.save(notificationEvent);
    }

    @Transactional
    public void updateAllAsRead(String accountId, String workspaceId) {
        Date before = DateHelper.now();
        log.info("Update all notification events as read has started. accountId: {}, workspaceId: {}, before: {}", accountId, workspaceId, before);
        notificationEventRepository.updateAllBeforeAsRead(accountId,workspaceId,before);
    }

    private NotificationEvent saveInitialEntry(NotificationTemplateDto notificationTemplateDto, NotificationSendVo notificationSendVo) {
        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setNotificationTemplateId(notificationTemplateDto.getNotificationTemplateId());
        notificationEvent.setAccountId(notificationSendVo.getAccountId());
        notificationEvent.setWorkspaceId(notificationSendVo.getWorkspaceId());
        notificationEvent.setTeamId(notificationSendVo.getTeamId());
        notificationEvent.setIsRead(Boolean.FALSE);
        notificationEvent.setIsSilent(notificationSendVo.getIsSilent());
        notificationEvent.setEventState(NotificationEventState.INITIALIZED);
        return notificationEventRepository.save(notificationEvent);
    }
}
