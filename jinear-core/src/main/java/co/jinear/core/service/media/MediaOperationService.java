package co.jinear.core.service.media;

import co.jinear.core.config.properties.FileStorageProperties;
import co.jinear.core.converter.media.AccessibleMediaDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.entity.media.Media;
import co.jinear.core.model.enumtype.media.MediaFileProviderType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.vo.media.InitializeMediaVo;
import co.jinear.core.model.vo.media.MediaInitializeResultVo;
import co.jinear.core.model.vo.media.RemoveMediaVo;
import co.jinear.core.repository.MediaRepository;
import co.jinear.core.service.media.fileoperation.MediaFileOperationServiceFactory;
import co.jinear.core.service.media.fileoperation.MediaFileOperationStrategy;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.FileStorageUtils;
import co.jinear.core.system.NormalizeHelper;
import co.jinear.core.system.RandomHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        Media media = saveMedia(initializeMediaVo, mediaKey, path, activeFileStorageType);

        MediaFileOperationStrategy mediaFileOperationStrategy = mediaFileOperationServiceFactory.getStrategy(activeFileStorageType);
        MediaInitializeResultVo mediaInitializeResultVo = mediaFileOperationStrategy.save(initializeMediaVo.getFile(), path);

        updateBucketName(media, mediaInitializeResultVo);
        if (MediaVisibilityType.PUBLIC.equals(media.getVisibility())) {
            mediaFileOperationStrategy.makePublic(mediaInitializeResultVo.getBucketName(), path);
        }
        return accessibleMediaDtoConverter.mapToAccessibleMediaDto(media);
    }

    @Transactional
    public AccessibleMediaDto deleteMedia(RemoveMediaVo removeMediaVo) {
        log.info("Delete media has started for removeMediaVo: {}", removeMediaVo);
        Media media = retrieveMedia(removeMediaVo.getMediaId());
        String passiveId = Optional.of(removeMediaVo)
                .map(RemoveMediaVo::getResponsibleAccountId)
                .map(passiveService::createSystemActionPassive)
                .orElseGet(passiveService::createSystemActionPassive);
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
        mediaFileOperationStrategy.makePublic(media.getBucketName(), media.getStoragePath());
    }

    @Transactional
    public void updateMediaAsPrivate(String mediaId) {
        log.info("Update media as private has started. mediaId: {}", mediaId);
        Media media = retrieveMedia(mediaId);
        media.setVisibility(MediaVisibilityType.PRIVATE);
        media.setPublicUntil(null);
        mediaRepository.save(media);

        MediaFileOperationStrategy mediaFileOperationStrategy = mediaFileOperationServiceFactory.getStrategy(media.getProviderType());
        mediaFileOperationStrategy.makePrivate(media.getBucketName(), media.getStoragePath());
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
        mediaFileOperationStrategy.makePublic(media.getBucketName(), media.getStoragePath());
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

    private Media saveMedia(InitializeMediaVo initializeMediaVo, String mediaKey, String path, MediaFileProviderType activeFileStorageType) {
        Media media = new Media();
        media.setMediaKey(mediaKey);
        media.setOwnerId(initializeMediaVo.getOwnerId());
        media.setRelatedObjectId(initializeMediaVo.getRelatedObjectId());
        media.setMediaOwnerType(initializeMediaVo.getMediaOwnerType());
        media.setFileType(initializeMediaVo.getFileType());
        media.setVisibility(initializeMediaVo.getVisibility());
        media.setStoragePath(path);
        media.setProviderType(activeFileStorageType);

        Optional.of(initializeMediaVo)
                .map(InitializeMediaVo::getFile)
                .map(MultipartFile::getContentType)
                .ifPresent(media::setContentType);

        String originalName = Optional.of(initializeMediaVo)
                .map(InitializeMediaVo::getFile)
                .map(MultipartFile::getOriginalFilename)
                .map(NormalizeHelper::normalizeUsernameReplaceSpaces)
                .orElse(UUID.randomUUID().toString());
        media.setOriginalName(originalName);

        Optional.of(initializeMediaVo)
                .map(InitializeMediaVo::getFile)
                .map(MultipartFile::getSize)
                .ifPresent(media::setSize);

        Media saved = mediaRepository.save(media);
        log.info("Media saved. mediaId: {}", saved.getMediaId());
        return saved;
    }

    private void updateBucketName(Media media, MediaInitializeResultVo mediaInitializeResultVo) {
        media.setBucketName(mediaInitializeResultVo.getBucketName());
        mediaRepository.save(media);
    }

    private String generatePath(InitializeMediaVo initializeMediaVo, String mediaKey) {
        String originalName = Optional.of(initializeMediaVo)
                .map(InitializeMediaVo::getFile)
                .map(MultipartFile::getOriginalFilename)
                .map(NormalizeHelper::normalizeUsernameReplaceSpaces)
                .orElse(UUID.randomUUID().toString());
        return FileStorageUtils.generatePath(initializeMediaVo.getMediaOwnerType(), initializeMediaVo.getRelatedObjectId(), initializeMediaVo.getFileType(), mediaKey, originalName);
    }
}
