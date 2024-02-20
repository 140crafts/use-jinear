package co.jinear.core.service.notification;

import co.jinear.core.converter.notification.NotificationTargetDtoConverter;
import co.jinear.core.model.dto.notification.NotificationTargetDto;
import co.jinear.core.model.entity.notification.NotificationTarget;
import co.jinear.core.repository.NotificationTargetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationTargetRetrieveService {

    private final NotificationTargetRepository notificationTargetRepository;
    private final NotificationTargetDtoConverter notificationTargetDtoConverter;

    public Optional<NotificationTarget> retrieveEntityBySessionId(String sessionInfoId) {
        log.info("Retrieve notification target entity has started. sessionInfoId:{}", sessionInfoId);
        return notificationTargetRepository.findBySessionInfoIdAndPassiveIdIsNull(sessionInfoId);
    }

    public Optional<NotificationTarget> retrieveEntityBySessionIdAndExternalTargetId(String sessionInfoId, String externalTargetId) {
        log.info("Retrieve notification target by session id and external target id. sessionInfoId:{}, externalTargetId: {}", sessionInfoId, externalTargetId);
        return notificationTargetRepository.findFirstBySessionInfoIdAndExternalTargetIdAndPassiveIdIsNull(sessionInfoId, externalTargetId);
    }

    public List<NotificationTargetDto> retrieveLatestAccountTargets(String accountId) {
        log.info("Retrieve latest account targets has started. accountId: {}", accountId);
        return notificationTargetRepository.findAllByAccountIdAndPassiveIdIsNullOrderByCreatedDateDesc(
                        accountId, PageRequest.of(0, 5))
                .stream()
                .map(notificationTargetDtoConverter::convert)
                .toList();
    }
}
