package co.jinear.core.manager.scheduledjob;

import co.jinear.core.service.media.MediaOperationService;
import co.jinear.core.service.media.MediaRetrieveService;
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

    @Async
    public void updateAllTemporaryPublicMedia() {
        log.info("Update all temporary public media has started.");
        List<String> expiredMedias = mediaRetrieveService.retrieveAllTemporaryPublicAndExpired();
        expiredMedias.forEach(this::updateMediaAsPrivate);
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
