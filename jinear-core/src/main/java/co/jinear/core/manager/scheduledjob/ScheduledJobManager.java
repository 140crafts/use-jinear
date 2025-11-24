package co.jinear.core.manager.scheduledjob;

import co.jinear.core.model.entity.media.Media;
import co.jinear.core.service.media.MediaOperationService;
import co.jinear.core.service.media.MediaRetrieveService;
import co.jinear.core.service.project.domain.ProjectDomainCnameOperatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledJobManager {

    private final MediaRetrieveService mediaRetrieveService;
    private final MediaOperationService mediaOperationService;
    private final ProjectDomainCnameOperatorService projectDomainCnameOperatorService;

    @Async
    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    public void updateAllTemporaryPublicMedia() {
        log.info("Update all temporary public media has started.");
        List<String> expiredMedias = mediaRetrieveService.retrieveAllTemporaryPublicAndExpired();
        expiredMedias.forEach(this::updateMediaAsPrivate);
    }

    @Async
    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void checkAndSyncCustomDomains() {
        log.info("Check and sync custom domains has started.");
        try {
            projectDomainCnameOperatorService.reCheckFailedDomains();
        } catch (Exception e) {
            log.error("Check and sync custom domains has failed.", e);
        }
    }

    @Async
    @Scheduled(fixedRate = 15, timeUnit = TimeUnit.MINUTES)
    public void checkAndUpdateWaitingForUploadMedia() {
        log.info("Retrieve waiting for upload media has started.");
        Page<Media> mediaPage = mediaRetrieveService.retrieveWaitingForUploadMedia();
        log.info("Waiting for upload medias retrieved. Total waiting: {}, Currently processing size: {}", mediaPage.getTotalElements(), mediaPage.getSize());
        mediaPage.forEach(media -> {
            try {
                mediaOperationService.checkExistenceAndUpdateStatus(media);
            } catch (Exception e) {
                log.error("Check existence and update status has failed.", e);
            }
        });
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
