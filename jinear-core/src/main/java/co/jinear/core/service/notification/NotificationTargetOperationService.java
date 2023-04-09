package co.jinear.core.service.notification;

import co.jinear.core.converter.notification.InitializeNotificationTargetVoConverter;
import co.jinear.core.converter.notification.NotificationTargetDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.notification.NotificationTargetDto;
import co.jinear.core.model.entity.notification.NotificationTarget;
import co.jinear.core.model.vo.notification.NotificationTargetInitializeVo;
import co.jinear.core.repository.NotificationTargetRepository;
import co.jinear.core.service.passive.PassiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationTargetOperationService {

    private final NotificationTargetRepository notificationTargetRepository;
    private final InitializeNotificationTargetVoConverter initializeNotificationTargetVoConverter;
    private final NotificationTargetDtoConverter notificationTargetDtoConverter;
    private final NotificationTargetRetrieveService notificationTargetRetrieveService;
    private final PassiveService passiveService;

    public NotificationTargetDto initializeNotificationTarget(NotificationTargetInitializeVo notificationTargetInitializeVo) {
        log.info("Initialize notification target has started. notificationTargetInitializeVo: {}", notificationTargetInitializeVo);
        NotificationTarget notificationTarget = initializeNotificationTargetVoConverter.convert(notificationTargetInitializeVo);
        NotificationTarget saved = notificationTargetRepository.save(notificationTarget);
        log.info("Initialize notification target has completed. notificationTargetId: {}", saved.getNotificationTargetId());
        return notificationTargetDtoConverter.convert(notificationTarget);
    }

    public String removeNotificationTarget(String sessionInfoId) {
        log.info("Remove notification target has started. sessionInfoId: {}", sessionInfoId);
        NotificationTarget notificationTarget = retrieveNotificationTarget(sessionInfoId);
        String passiveId = passiveService.createUserActionPassive();
        notificationTarget.setPassiveId(passiveId);
        notificationTargetRepository.save(notificationTarget);
        log.info("Remove notification target has completed. passiveId: {}", passiveId);
        return passiveId;
    }

    private NotificationTarget retrieveNotificationTarget(String sessionInfoId) {
        return notificationTargetRetrieveService.retrieveEntityBySessionId(sessionInfoId)
                .orElseThrow(NotFoundException::new);
    }
}
