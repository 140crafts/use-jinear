package co.jinear.core.service.task.media;

import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.service.media.MediaRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskMediaRetrieveService {

    private final MediaRetrieveService mediaRetrieveService;

    public MediaDto retrieve(String taskId, String mediaId) {
        log.info("Retrieve task media has started. taskId: {}, mediaId: {}", taskId, mediaId);
        return mediaRetrieveService.retrieveMediaWithMediaIdAndRelatedObjectId(mediaId, taskId);
    }

    public MediaDto retrieveInclDeleted(String taskId, String mediaId) {
        log.info("Retrieve task media including deleted has started. taskId: {}, mediaId: {}", taskId, mediaId);
        return mediaRetrieveService.retrieveMediaWithMediaIdAndRelatedObjectIdIncludingPassive(mediaId, taskId);
    }

    public AccessibleMediaDto retrieveAccessible(String taskId, String mediaId) {
        log.info("Retrieve task media has started. taskId: {}, mediaId: {}", taskId, mediaId);
        return mediaRetrieveService.retrieveAccessibleMediaWithMediaIdAndRelatedObjectId(mediaId, taskId);
    }

    public List<MediaDto> retrieveTaskRelatedMedia(String taskId) {
        log.info("Retrieve task related media has started. taskId: {}", taskId);
        return mediaRetrieveService.retrieveAllByRelatedObject(taskId, FileType.TASK_FILE);
    }

    public String retrievePublicDownloadLink(AccessibleMediaDto accessibleMediaDto) {
        log.info("Retrieve public link for task media has started.");
        return mediaRetrieveService.retrievePublicDownloadLink(accessibleMediaDto);
    }
}
