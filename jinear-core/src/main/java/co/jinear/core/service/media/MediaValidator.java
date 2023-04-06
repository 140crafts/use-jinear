package co.jinear.core.service.media;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.system.gcloud.vision.CloudVision;
import co.jinear.core.system.gcloud.vision.GVisionApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaValidator {

    public void validateForSafeImage(MultipartFile file) {
        if (containsExplicitContent(file)) {
            log.info("File contains explicit content.");
            throw new BusinessException("media.explicit.content");
        }
    }

    private boolean containsExplicitContent(MultipartFile file) {
        log.info("Contains explicit content for file has started.");
        GVisionApiResponse visionApiResponse = CloudVision.analyzeImage(file);
        log.info("Contains explicit content for file has finished. visionApiResponse: {}", visionApiResponse);
        return !visionApiResponse.getIsSafe();
    }
}
