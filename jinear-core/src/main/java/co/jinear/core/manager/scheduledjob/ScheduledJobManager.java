package co.jinear.core.manager.scheduledjob;

import co.jinear.core.service.media.MediaOperationService;
import co.jinear.core.service.media.MediaRetrieveService;
import co.jinear.core.service.project.domain.ProjectDomainCnameOperatorService;
import co.jinear.core.system.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledJobManager {

    private final MediaRetrieveService mediaRetrieveService;
    private final MediaOperationService mediaOperationService;
    private final ProjectDomainCnameOperatorService projectDomainCnameOperatorService;

    @Async
    public void updateAllTemporaryPublicMedia() {
        log.info("Update all temporary public media has started.");
        List<String> expiredMedias = mediaRetrieveService.retrieveAllTemporaryPublicAndExpired();
        expiredMedias.forEach(this::updateMediaAsPrivate);
    }

    @Async
    public void checkAndSyncCustomDomains() {
        log.info("Check and sync custom domains has started.");
        Try findAvailableAndSyncTry = Try.of(projectDomainCnameOperatorService::findAvailableAndSync);
        Try reCheckFailedDomainsTry = Try.of(projectDomainCnameOperatorService::reCheckFailedDomains);
        findAvailableAndSyncTry.throwIfFailed();
        reCheckFailedDomainsTry.throwIfFailed();
    }

    private void updateMediaAsPrivate(String mediaId) {
        try {
            log.info("Update media as private has started for mediaId: {}", mediaId);
            mediaOperationService.updateMediaAsPrivate(mediaId);
        } catch (Exception e) {
            log.error("[SCHEDULED-MANAGER] Update media as private has failed.", e);
        }
    }
}
