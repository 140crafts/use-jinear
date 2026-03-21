package co.jinear.core.converter.media;

import co.jinear.core.model.entity.media.Media;
import co.jinear.core.model.enumtype.media.MediaFileOwnershipStatusType;
import co.jinear.core.model.enumtype.media.MediaFileProviderType;
import co.jinear.core.model.enumtype.media.MediaFileUploadMethodType;
import co.jinear.core.model.enumtype.media.MediaFileUploadStatusType;
import co.jinear.core.model.vo.media.BaseInitializeMediaVo;
import co.jinear.core.model.vo.media.InitializeMediaVo;
import co.jinear.core.model.vo.media.InitializeWaitingMediaVo;
import co.jinear.core.system.NormalizeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class MediaEntityConverter {

    private static final int DEFAULT_EXPIRE_TIME = 30;

    public Media mapToEntity(InitializeMediaVo initializeMediaVo,
                             String mediaKey,
                             String path,
                             MediaFileProviderType activeFileStorageType,
                             MediaFileOwnershipStatusType ownershipStatus) {
        Media media = mapBaseInitializeMediaVoToEntity(initializeMediaVo, mediaKey, path, activeFileStorageType, ownershipStatus);

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

        media.setUploadMethod(MediaFileUploadMethodType.LEGACY);
        media.setUploadStatus(MediaFileUploadStatusType.COMPLETED);
        return media;
    }

    public Media mapToEntity(InitializeWaitingMediaVo initializeWaitingMediaVo,
                             String mediaKey,
                             String path,
                             MediaFileProviderType activeFileStorageType) {
        Media media = mapBaseInitializeMediaVoToEntity(initializeWaitingMediaVo, mediaKey, path, activeFileStorageType, initializeWaitingMediaVo.getOwnershipStatus());

        String originalName = Optional.of(initializeWaitingMediaVo)
                .map(InitializeWaitingMediaVo::getOriginalName)
                .orElse(UUID.randomUUID().toString());
        media.setOriginalName(originalName);

        media.setContentType(initializeWaitingMediaVo.getContentType());
        media.setUploadMethod(MediaFileUploadMethodType.PRESIGNED_URL);
        media.setUploadStatus(MediaFileUploadStatusType.WAITING);
        media.setUploadWindowExpiresAt(ZonedDateTime.now().plusMinutes(DEFAULT_EXPIRE_TIME));
        media.setSize(initializeWaitingMediaVo.getFileSize());

        return media;
    }

    public Media mapToEntity(BaseInitializeMediaVo baseVo,
                             String mediaKey,
                             String path,
                             MediaFileProviderType activeFileStorageType,
                             MediaFileOwnershipStatusType ownershipStatus,
                             String contentType,
                             String originalName) {
        Media media = mapBaseInitializeMediaVoToEntity(baseVo, mediaKey, path, activeFileStorageType, ownershipStatus);
        media.setContentType(contentType);
        media.setOriginalName(originalName);
        media.setUploadMethod(MediaFileUploadMethodType.LEGACY);
        media.setUploadStatus(MediaFileUploadStatusType.COMPLETED);
        return media;
    }

    private Media mapBaseInitializeMediaVoToEntity(BaseInitializeMediaVo baseInitializeMediaVo,
                                                   String mediaKey,
                                                   String path,
                                                   MediaFileProviderType activeFileStorageType,
                                                   MediaFileOwnershipStatusType ownershipStatus) {
        Media media = new Media();
        media.setMediaKey(mediaKey);
        media.setOwnerId(baseInitializeMediaVo.getOwnerId());
        media.setRelatedObjectId(baseInitializeMediaVo.getRelatedObjectId());
        media.setMediaOwnerType(baseInitializeMediaVo.getMediaOwnerType());
        media.setFileType(baseInitializeMediaVo.getFileType());
        media.setVisibility(baseInitializeMediaVo.getVisibility());
        media.setStoragePath(path);
        media.setProviderType(activeFileStorageType);
        media.setOwnershipStatus(ownershipStatus);
        return media;
    }
}
