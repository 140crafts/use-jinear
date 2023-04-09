package co.jinear.core.service.notification;

import co.jinear.core.model.entity.notification.NotificationTarget;
import co.jinear.core.repository.NotificationTargetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationTargetRetrieveService {

    private final NotificationTargetRepository notificationTargetRepository;

    public Optional<NotificationTarget> retrieveEntityBySessionId(String sessionInfoId) {
        log.info("Retrieve notification target entity has started. sessionInfoId:{}", sessionInfoId);
        return notificationTargetRepository.findBySessionInfoIdAndPassiveIdIsNull(sessionInfoId);
    }

    public boolean hasNotificationTargetWithSessionInfoId(String sessionInfoId) {
        log.info("Check notification target exists has started. sessionInfoId:{}", sessionInfoId);
        return notificationTargetRepository.countAllBySessionInfoIdAndPassiveIdIsNull(sessionInfoId)>0L;
    }
}
