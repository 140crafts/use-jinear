package co.jinear.core.service.media;

import co.jinear.core.config.properties.GCloudProperties;
import co.jinear.core.converter.media.AccessibleMediaDtoConverter;
import co.jinear.core.converter.media.MediaDtoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.entity.media.Media;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.vo.media.InitializeMediaVo;
import co.jinear.core.model.vo.media.RemoveMediaVo;
import co.jinear.core.repository.MediaRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.FileStorageUtils;
import co.jinear.core.system.NormalizeHelper;
import co.jinear.core.system.RandomHelper;
import co.jinear.core.system.Try;
import co.jinear.core.system.gcloud.storage.CloudStorage;
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

    private final MediaRepository mediaRepository;
    private final GCloudProperties gCloudProperties;
    private final PassiveService passiveService;
    private final MediaRetrieveService mediaRetrieveService;
    private final MediaDtoConverter mediaDtoConverter;
    private final AccessibleMediaDtoConverter accessibleMediaDtoConverter;

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
        String mediaKey = RandomHelper.generateULID();
        String path = generatePath(initializeMediaVo, mediaKey);
        String bucketName = gCloudProperties.getBucketName();
        Media media = saveMedia(initializeMediaVo, mediaKey, path, bucketName);
        uploadToStorage(bucketName, path, initializeMediaVo.getFile());
        if (MediaVisibilityType.PUBLIC.equals(media.getVisibility())) {
            makePublicOnStorage(bucketName, path);
        }
        return accessibleMediaDtoConverter.mapToAccessibleMediaDto(media);
    }

    @Transactional
    public AccessibleMediaDto deleteMedia(RemoveMediaVo removeMediaVo) {
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
        return accessibleMediaDtoConverter.mapToAccessibleMediaDto(saved);
    }

    @Transactional
    public void updateMediaAsTemporaryPublic(String mediaId, ZonedDateTime publicUntil) {
        log.info("Update media as temporary public has started. mediaId: {}, publicUntil: {}", mediaId, publicUntil);
        Media media = retrieveMedia(mediaId);

        String path = FileStorageUtils.generatePath(media.getMediaOwnerType(), media.getRelatedObjectId(), media.getFileType(), media.getMediaKey(), media.getOriginalName());
        String bucketName = gCloudProperties.getBucketName();
        makePublicOnStorage(bucketName, path);

        media.setVisibility(MediaVisibilityType.TEMP_PUBLIC);
        media.setPublicUntil(publicUntil);
        mediaRepository.save(media);
    }

    @Transactional
    public void updateMediaAsPrivate(String mediaId) {
        log.info("Update media as private has started. mediaId: {}", mediaId);
        Media media = retrieveMedia(mediaId);

        String path = FileStorageUtils.generatePath(media.getMediaOwnerType(), media.getRelatedObjectId(), media.getFileType(), media.getMediaKey(), media.getOriginalName());
        String bucketName = gCloudProperties.getBucketName();
        makePrivateOnStorage(bucketName, path);

        media.setVisibility(MediaVisibilityType.PRIVATE);
        media.setPublicUntil(null);
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

    private Media saveMedia(InitializeMediaVo initializeMediaVo, String mediaKey, String path, String bucketName) {
        Media media = new Media();
        media.setMediaKey(mediaKey);
        media.setOwnerId(initializeMediaVo.getOwnerId());
        media.setRelatedObjectId(initializeMediaVo.getRelatedObjectId());
        media.setMediaOwnerType(initializeMediaVo.getMediaOwnerType());
        media.setFileType(initializeMediaVo.getFileType());
        media.setVisibility(initializeMediaVo.getVisibility());
        media.setBucketName(bucketName);
        media.setStoragePath(path);

        String originalName = Optional.of(initializeMediaVo)
                .map(InitializeMediaVo::getFile)
                .map(MultipartFile::getOriginalFilename)
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

    private void uploadToStorage(String bucketName, String path, MultipartFile file) {
        log.info("Upload media to storage has started. bucketName: {}, path: {}", bucketName, path);
        try {
            CloudStorage.uploadObject(bucketName, path, file);
        } catch (Exception e) {
            log.error("CloudStorage uploadObject", e);
            throw new BusinessException();
        }
    }

    private void makePublicOnStorage(String bucketName, String path) {
        log.info("Make media physically public on storage has started. bucketName: {}, path: {}", bucketName, path);
        try {
            CloudStorage.makeObjectPublic(bucketName, path);
        } catch (Exception e) {
            log.error("Make media physically public on storage has failed.", e);
            throw new BusinessException();
        }
    }

    private void makePrivateOnStorage(String bucketName, String path) {
        log.info("Make media physically private on storage has started. bucketName: {}, path: {}", bucketName, path);
        try {
            CloudStorage.makeObjectPrivate(bucketName, path);
        } catch (Exception e) {
            log.error("Make media physically private on storage has failed.", e);
            throw new BusinessException();
        }
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
