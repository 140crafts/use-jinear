package co.jinear.core.service.notification;

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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventOperationService {

    private final NotificationEventRepository notificationEventRepository;

    @Transactional
    public NotificationEvent initialize(NotificationSendVo notificationSendVo) {
        log.info("Notification event initialize has started. notificationSendVo: {}", notificationSendVo);
        NotificationEvent notificationEvent = saveInitialEntry(notificationSendVo);
        log.info("Notification initialized. notificationEventId: {}", notificationEvent.getNotificationEventId());
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
        notificationEventRepository.updateAllBeforeAsRead(accountId, workspaceId, before);
    }

    @Transactional
    public void updateAllAsRead(String accountId, String workspaceId, String teamId) {
        Date before = DateHelper.now();
        log.info("Update all notification events as read has started. accountId: {}, workspaceId: {}, teamId: {}, before: {}", accountId, workspaceId, teamId, before);
        notificationEventRepository.updateAllFromTeamBeforeAsRead(accountId, workspaceId, teamId, before);
    }

    private NotificationEvent saveInitialEntry(NotificationSendVo notificationSendVo) {
        Boolean isSilent = Optional.of(notificationSendVo).map(NotificationSendVo::getIsSilent).orElse(Boolean.TRUE);
        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setAccountId(notificationSendVo.getAccountId());
        notificationEvent.setWorkspaceId(notificationSendVo.getWorkspaceId());
        notificationEvent.setTeamId(notificationSendVo.getTeamId());
        notificationEvent.setTaskId(notificationSendVo.getTaskId());
        notificationEvent.setIsRead(Boolean.FALSE);
        notificationEvent.setIsSilent(isSilent);
        notificationEvent.setEventState(NotificationEventState.INITIALIZED);
        notificationEvent.setTitle(notificationSendVo.getTitle());
        notificationEvent.setText(notificationSendVo.getText());
        notificationEvent.setLaunchUrl(notificationSendVo.getLaunchUrl());
        notificationEvent.setNotificationType(notificationSendVo.getNotificationType());
        return notificationEventRepository.save(notificationEvent);
    }
}
