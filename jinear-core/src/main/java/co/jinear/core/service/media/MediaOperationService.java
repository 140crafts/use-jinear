package co.jinear.core.service.media;

import co.jinear.core.config.properties.FileStorageProperties;
import co.jinear.core.converter.media.AccessibleMediaDtoConverter;
import co.jinear.core.converter.media.MediaEntityConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.dto.media.WaitingMediaResultDto;
import co.jinear.core.model.entity.media.Media;
import co.jinear.core.model.enumtype.media.MediaFileOwnershipStatusType;
import co.jinear.core.model.enumtype.media.MediaFileProviderType;
import co.jinear.core.model.enumtype.media.MediaFileUploadStatusType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.vo.media.*;
import co.jinear.core.repository.MediaRepository;
import co.jinear.core.service.media.fileoperation.MediaFileOperationServiceFactory;
import co.jinear.core.service.media.fileoperation.MediaFileOperationStrategy;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.FileStorageUtils;
import co.jinear.core.system.NormalizeHelper;
import co.jinear.core.system.RandomHelper;
import co.jinear.core.system.util.ContentTypeFinder;
import co.jinear.core.system.util.UrlContentInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaOperationService {

    public static final Long PUBLIC_WINDOW_IN_SECONDS = 10L;

    private final MediaRepository mediaRepository;
    private final PassiveService passiveService;
    private final MediaRetrieveService mediaRetrieveService;
    private final AccessibleMediaDtoConverter accessibleMediaDtoConverter;
    private final FileStorageProperties fileStorageProperties;
    private final MediaFileOperationServiceFactory mediaFileOperationServiceFactory;
    private final MediaEntityConverter mediaEntityConverter;

    @Transactional
    public AccessibleMediaDto changeProfilePicture(InitializeMediaVo initializeMediaVo) {
        log.info("Change profile picture has started. initializeMediaVo: {}", initializeMediaVo);
        Optional<AccessibleMediaDto> mediaDtoOptional = mediaRetrieveService.retrieveProfilePictureOptional(initializeMediaVo.getRelatedObjectId());
        AccessibleMediaDto profilePicture = initializeMedia(initializeMediaVo);
        deleteProfilePictureIfExists(initializeMediaVo, mediaDtoOptional);
        return profilePicture;
    }

    @Transactional
    public AccessibleMediaDto initializeMedia(InitializeMediaVo initializeMediaVo) {
        log.info("Initialize media has started. initializeMediaVo: {}", initializeMediaVo);
        MediaFileProviderType activeFileStorageType = fileStorageProperties.getActiveFileStorageType();
        String mediaKey = RandomHelper.generateULID();
        String path = generatePath(initializeMediaVo, mediaKey);
        Media media = mediaEntityConverter.mapToEntity(initializeMediaVo, mediaKey, path, activeFileStorageType, initializeMediaVo.getOwnershipStatus());
        MediaFileOperationStrategy mediaFileOperationStrategy = mediaFileOperationServiceFactory.getStrategy(activeFileStorageType);
        MediaInitializeResultVo mediaInitializeResultVo = mediaFileOperationStrategy.save(initializeMediaVo.getFile(), initializeMediaVo.getVisibility(), path);
        updateBucketName(media, mediaInitializeResultVo.getBucketName());
        return accessibleMediaDtoConverter.mapToAccessibleMediaDto(media);
    }

    @Transactional
    public AccessibleMediaDto initializeMediaFromUrl(BaseInitializeMediaVo baseVo, URL url) {
        log.info("Initialize media from URL has started. url: {}, baseVo: {}", url, baseVo);
        MediaFileProviderType activeFileStorageType = fileStorageProperties.getActiveFileStorageType();
        String mediaKey = RandomHelper.generateULID();
        String originalName = extractFileNameFromUrl(url);

        String contentType = Optional.of(url)
                .map(ContentTypeFinder::findUrlContentInfo)
                .map(UrlContentInfo::getContentType)
                .orElse(null);

        String path = generatePath(baseVo, mediaKey, originalName);
        Media media = mediaEntityConverter.mapToEntity(baseVo, mediaKey, path, activeFileStorageType, baseVo.getOwnershipStatus(), contentType, originalName);
        MediaFileOperationStrategy mediaFileOperationStrategy = mediaFileOperationServiceFactory.getStrategy(activeFileStorageType);
        MediaInitializeResultVo mediaInitializeResultVo = mediaFileOperationStrategy.save(url, baseVo.getVisibility(), path, contentType);
        updateBucketName(media, mediaInitializeResultVo.getBucketName());
        return accessibleMediaDtoConverter.mapToAccessibleMediaDto(media);
    }

    public WaitingMediaResultDto initializeWaitingMediaAndGetPresignedUploadUrl(InitializeWaitingMediaVo initializeWaitingMediaVo) {
        log.info("Initialize waiting media has started. initializeWaitingMediaVo: {}", initializeWaitingMediaVo);
        MediaFileProviderType activeFileStorageType = fileStorageProperties.getActiveFileStorageType();

        String mediaKey = RandomHelper.generateULID();
        String path = generatePath(initializeWaitingMediaVo, mediaKey, initializeWaitingMediaVo.getOriginalName());
        Media media = mediaEntityConverter.mapToEntity(initializeWaitingMediaVo, mediaKey, path, activeFileStorageType);

        MediaFileOperationStrategy mediaFileOperationStrategy = mediaFileOperationServiceFactory.getStrategy(activeFileStorageType);
        WaitingMediaResultVo waitingMediaResultVo = mediaFileOperationStrategy.presignUrl(path, media.getContentType(), initializeWaitingMediaVo.getVisibility(), initializeWaitingMediaVo.getFileSize());

        updateUploadWindowExpiresAt(media, waitingMediaResultVo.getUploadWindowExpiresAt());
        updateBucketName(media, waitingMediaResultVo.getBucketName());

        return WaitingMediaResultDto
                .builder()
                .presignedUrl(waitingMediaResultVo.getPresignedUrl().toString())
                .mediaId(media.getMediaId())
                .uploadWindowExpiresAt(waitingMediaResultVo.getUploadWindowExpiresAt())
                .build();
    }

    @Transactional
    public AccessibleMediaDto deleteMedia(RemoveMediaVo removeMediaVo) {
        log.info("Delete media has started for removeMediaVo: {}", removeMediaVo);
        Media media = retrieveMedia(removeMediaVo.getMediaId());
        String passiveId = Optional.of(removeMediaVo)
                .map(RemoveMediaVo::getExistingPassiveId)
                .orElseGet(() ->
                        Optional.of(removeMediaVo)
                                .map(RemoveMediaVo::getResponsibleAccountId)
                                .map(passiveService::createSystemActionPassive)
                                .orElseGet(passiveService::createSystemActionPassive));
        media.setPassiveId(passiveId);
        Media saved = mediaRepository.save(media);
        log.info("Delete media entity saved. passiveId: {}, mediaId: {}", passiveId, saved.getMediaId());
        MediaFileOperationStrategy mediaFileOperationStrategy = mediaFileOperationServiceFactory.getStrategy(media.getProviderType());
        mediaFileOperationStrategy.remove(media.getBucketName(), media.getStoragePath());
        return accessibleMediaDtoConverter.mapToAccessibleMediaDto(saved);
    }

    @Transactional
    public void updateMediaAsTemporaryPublic(String mediaId) {
        ZonedDateTime publicUntil = ZonedDateTime.now().plusSeconds(PUBLIC_WINDOW_IN_SECONDS);
        log.info("Update media as temporary public has started. mediaId: {}, publicUntil: {}", mediaId, publicUntil);
        Media media = retrieveMedia(mediaId);
        media.setVisibility(MediaVisibilityType.TEMP_PUBLIC);
        media.setPublicUntil(publicUntil);
        mediaRepository.save(media);

        MediaFileOperationStrategy mediaFileOperationStrategy = mediaFileOperationServiceFactory.getStrategy(media.getProviderType());
        String newBucketName = mediaFileOperationStrategy.makePublic(media.getBucketName(), media.getStoragePath());
        updateBucketName(media, newBucketName);
    }

    @Transactional
    public void updateMediaAsPrivate(String mediaId) {
        log.info("Update media as private has started. mediaId: {}", mediaId);
        Media media = retrieveMedia(mediaId);
        media.setVisibility(MediaVisibilityType.PRIVATE);
        media.setPublicUntil(null);
        mediaRepository.save(media);

        MediaFileOperationStrategy mediaFileOperationStrategy = mediaFileOperationServiceFactory.getStrategy(media.getProviderType());
        String newBucketName = mediaFileOperationStrategy.makePrivate(media.getBucketName(), media.getStoragePath());
        updateBucketName(media, newBucketName);
    }

    @Transactional
    public void updateMediaVisibility(String mediaId, MediaVisibilityType mediaVisibilityType) {
        log.info("Update media visibility has started. mediaId: {}, mediaVisibilityType: {}", mediaId, mediaVisibilityType);
        Media media = retrieveMedia(mediaId);
        media.setVisibility(mediaVisibilityType);
        Optional.ofNullable(mediaVisibilityType)
                .filter(MediaVisibilityType.TEMP_PUBLIC::equals)
                .map(tempPublic -> ZonedDateTime.now().plusSeconds(PUBLIC_WINDOW_IN_SECONDS))
                .ifPresentOrElse(media::setPublicUntil, () -> media.setPublicUntil(null));
        mediaRepository.save(media);

        MediaFileOperationStrategy mediaFileOperationStrategy = mediaFileOperationServiceFactory.getStrategy(media.getProviderType());
        String newBucketName = MediaVisibilityType.PRIVATE.equals(mediaVisibilityType) ? mediaFileOperationStrategy.makePrivate(media.getBucketName(), media.getStoragePath()) : mediaFileOperationStrategy.makePublic(media.getBucketName(), media.getStoragePath());
        updateBucketName(media, newBucketName);
    }

    public void updateLateOwnerIdAndOwnershipStatus(String relatedObjectId, String mediaKey, String lateOwnerId) {
        log.info("Update late owner id and ownership status has started. relatedObjectId: {}, mediaKey: {}, lateOwnerId: {}", relatedObjectId, mediaKey, lateOwnerId);
        mediaRetrieveService.retrieveEntityWithRelatedObjectIdAndMediaKey(relatedObjectId, mediaKey)
                .ifPresent(media -> {
                    log.info("Media found updating.");
                    media.setLateOwnerId(lateOwnerId);
                    media.setOwnershipStatus(MediaFileOwnershipStatusType.OWNED);
                    mediaRepository.save(media);
                });
    }

    public void updateAsUploadCompleted(String mediaId) {
        log.info("Update as upload completed has started. mediaId: {}", mediaId);
        Media media = retrieveMedia(mediaId);
        if (!MediaFileUploadStatusType.COMPLETED.equals(media.getUploadStatus())) {
            media.setUploadStatus(MediaFileUploadStatusType.COMPLETED);
            mediaRepository.save(media);
        }
    }

    public void checkExistenceAndUpdateStatus(Media media) {
        log.info("Check existence and update status has started. mediaId: {}", media.getMediaId());
        MediaFileProviderType activeFileStorageType = fileStorageProperties.getActiveFileStorageType();
        MediaFileOperationStrategy mediaFileOperationStrategy = mediaFileOperationServiceFactory.getStrategy(activeFileStorageType);
        boolean mediaExists = mediaFileOperationStrategy.doesObjectExists(media.getBucketName(), media.getStoragePath());
        ZonedDateTime uploadWindowExpiresAt = media.getUploadWindowExpiresAt();
        log.info("mediaExists: {}, uploadWindowExpiresAt: {}", mediaExists, uploadWindowExpiresAt);
        if (!mediaExists && ZonedDateTime.now().isAfter(uploadWindowExpiresAt)) {
            log.info("Updating upload status as failed.");
            media.setUploadStatus(MediaFileUploadStatusType.FAILED);
        } else if (mediaExists) {
            log.info("Updating upload status as completed.");
            media.setUploadStatus(MediaFileUploadStatusType.COMPLETED);
        }
        mediaRepository.save(media);
    }

    private void deleteProfilePictureIfExists(InitializeMediaVo initializeMediaVo, Optional<AccessibleMediaDto> mediaDtoOptional) {
        mediaDtoOptional.map(internalMediaDto -> {
                    RemoveMediaVo removeMediaVo = new RemoveMediaVo();
                    removeMediaVo.setMediaId(internalMediaDto.getMediaId());
                    removeMediaVo.setResponsibleAccountId(initializeMediaVo.getOwnerId());
                    return removeMediaVo;
                })
                .ifPresent(this::deleteMedia);
    }

    private Media retrieveMedia(String mediaId) {
        return mediaRepository.findByMediaIdAndPassiveIdIsNull(mediaId).orElseThrow(NotFoundException::new);
    }

    private void updateBucketName(Media media, String bucketName) {
        if (!StringUtils.equals(bucketName, media.getBucketName())) {
            media.setBucketName(bucketName);
            mediaRepository.save(media);
        }
    }

    private void updateUploadWindowExpiresAt(Media media, ZonedDateTime uploadWindowExpiresAt) {
        media.setUploadWindowExpiresAt(uploadWindowExpiresAt);
        mediaRepository.save(media);
    }

    private String generatePath(InitializeMediaVo initializeMediaVo, String mediaKey) {
        String originalName = Optional.of(initializeMediaVo)
                .map(InitializeMediaVo::getFile)
                .map(MultipartFile::getOriginalFilename)
                .map(NormalizeHelper::normalizeUsernameReplaceSpaces)
                .orElse(UUID.randomUUID().toString());
        return generatePath(initializeMediaVo, mediaKey, originalName);
    }

    private String generatePath(BaseInitializeMediaVo baseInitializeMediaVo, String mediaKey, String originalName) {
        return FileStorageUtils.generatePath(baseInitializeMediaVo.getMediaOwnerType(), baseInitializeMediaVo.getRelatedObjectId(), baseInitializeMediaVo.getFileType(), mediaKey, originalName);
    }

    private String extractFileNameFromUrl(URL url) {
        String path = url.getPath();
        int lastSlash = path.lastIndexOf("/");
        String name = lastSlash >= 0 ? path.substring(lastSlash + 1) : path;
        return name.isEmpty() ? UUID.randomUUID().toString() : NormalizeHelper.normalizeUsernameReplaceSpaces(name);
    }
}
