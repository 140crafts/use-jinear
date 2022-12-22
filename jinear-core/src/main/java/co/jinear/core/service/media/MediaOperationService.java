package co.jinear.core.service.media;

import co.jinear.core.config.properties.GCloudProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.entity.media.Media;
import co.jinear.core.model.vo.media.InitializeMediaVo;
import co.jinear.core.model.vo.media.RemoveMediaVo;
import co.jinear.core.repository.MediaRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.Try;
import co.jinear.core.system.gcloud.storage.CloudStorage;
import co.jinear.core.system.gcloud.vision.CloudVision;
import co.jinear.core.system.gcloud.vision.GVisionApiResponse;
import co.jinear.core.system.util.DateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaOperationService {
    private static final String FS_PREFIX = "FILE_STORAGE";
    private static final String UNDERLINE = "_";
    private final MediaRepository mediaRepository;
    private final GCloudProperties gCloudProperties;
    private final PassiveService passiveService;
    private final MediaRetrieveService mediaRetrieveService;
    private final ModelMapper modelMapper;

    @Transactional
    public MediaDto changeProfilePicture(InitializeMediaVo initializeMediaVo) {
        log.info("Change profile picture has started. initializeMediaVo: {}",initializeMediaVo);
        Optional<MediaDto> mediaDtoOptional = mediaRetrieveService.retrieveProfilePictureOptional(initializeMediaVo.getRelatedObjectId());
        MediaDto profilePicture = initializeMedia(initializeMediaVo);
        deleteProfilePictureIfExists(initializeMediaVo, mediaDtoOptional);
        return profilePicture;
    }

    @Transactional
    public MediaDto initializeMedia(InitializeMediaVo initializeMediaVo) {
        log.info("Initialize media has started. initializeMediaVo: {}", initializeMediaVo);
        validateForSafeImage(initializeMediaVo.getFile());
        String path = generatePath(initializeMediaVo);
        String bucketName = gCloudProperties.getBucketName();
        Media media = saveMedia(initializeMediaVo, path, bucketName);
        uploadAndMakePublic(bucketName, path, initializeMediaVo.getFile());
        return modelMapper.map(media, MediaDto.class);
    }

    @Transactional
    public void deleteMedia(RemoveMediaVo removeMediaVo) {
        log.info("Delete media has started for removeMediaVo: {}", removeMediaVo);
        Media media = retrieveMedia(removeMediaVo.getMediaId());
        String passiveId = passiveService.createSystemActionPassive(removeMediaVo.getResponsibleAccountId());
        media.setPassiveId(passiveId);
        Media saved = mediaRepository.save(media);
        log.info("Delete media entity saved. passiveId: {}, mediaId: {}", passiveId, saved.getMediaId());
        Try deleteTry = Try.of(() -> CloudStorage.deleteObject(media.getBucketName(), media.getStoragePath()));
        Optional.of(deleteTry)
                .filter(Try::isFailure)
                .map(Try::getThrownMessage)
                .ifPresent(throwable -> log.error("Delete object has failed.", throwable));
    }

    private void deleteProfilePictureIfExists(InitializeMediaVo initializeMediaVo, Optional<MediaDto> mediaDtoOptional) {
        mediaDtoOptional.ifPresent(mediaDto -> {
            RemoveMediaVo removeMediaVo = new RemoveMediaVo();
            removeMediaVo.setMediaId(mediaDto.getMediaId());
            removeMediaVo.setResponsibleAccountId(initializeMediaVo.getOwnerId());
            deleteMedia(removeMediaVo);
        });
    }

    private Media retrieveMedia(String mediaId) {
        return mediaRepository.findByMediaIdAndPassiveIdIsNull(mediaId).orElseThrow(NotFoundException::new);
    }

    private Media saveMedia(InitializeMediaVo initializeMediaVo, String path, String bucketName) {
        Media media = new Media();
        media.setOwnerId(initializeMediaVo.getOwnerId());
        media.setRelatedObjectId(initializeMediaVo.getRelatedObjectId());
        media.setMediaOwnerType(initializeMediaVo.getMediaOwnerType());
        media.setFileType(initializeMediaVo.getFileType());
        media.setBucketName(bucketName);
        media.setStoragePath(path);
        Media saved = mediaRepository.save(media);
        log.info("Media saved. mediaId: {}", saved.getMediaId());
        return saved;
    }

    private void uploadAndMakePublic(String bucketName, String path, MultipartFile file) {
        log.info("Upload file and make public has started. path: {}, bucketName: {}", path, bucketName);
        try {
            CloudStorage.uploadObject(bucketName, path, file);
            CloudStorage.makeObjectPublic(bucketName, path);
        } catch (Exception e) {
            log.error("CloudStorage uploadObject", e);
            throw new BusinessException();
        }
    }

    private void validateForSafeImage(MultipartFile file) {
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

    private String generatePath(InitializeMediaVo initializeMediaVo) {
        String today = DateHelper.getFileDate(DateHelper.now());
        String fileFame = UUID.randomUUID().toString();
        StringBuilder sb = new StringBuilder();
        return sb.append(FS_PREFIX)
                .append(java.io.File.separator).append(initializeMediaVo.getMediaOwnerType().name())
                .append(java.io.File.separator).append(initializeMediaVo.getRelatedObjectId())
                .append(java.io.File.separator).append(initializeMediaVo.getFileType().name())
                .append(java.io.File.separator).append(today).append(UNDERLINE).append(fileFame)
                .toString();
    }
}
