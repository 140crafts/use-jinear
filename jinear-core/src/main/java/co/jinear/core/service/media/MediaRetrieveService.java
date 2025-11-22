package co.jinear.core.service.media;

import co.jinear.core.converter.media.AccessibleMediaDtoConverter;
import co.jinear.core.converter.media.MediaDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.dto.media.MediaDto;
import co.jinear.core.model.entity.media.Media;
import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaFileUploadMethodType;
import co.jinear.core.model.enumtype.media.MediaFileUploadStatusType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.repository.MediaRepository;
import co.jinear.core.service.media.fileoperation.MediaFileOperationServiceFactory;
import co.jinear.core.service.media.fileoperation.MediaFileOperationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaRetrieveService {

    private final MediaRepository mediaRepository;
    private final MediaDtoConverter mediaDtoConverter;
    private final AccessibleMediaDtoConverter accessibleMediaDtoConverter;
    private final MediaFileOperationServiceFactory mediaFileOperationServiceFactory;

    public Optional<AccessibleMediaDto> retrieveProfilePictureOptional(String relatedObjectId) {
        log.info("Retrieve profile picture optional has started. relatedObjectId: {}", relatedObjectId);
        return retrieveAccessibleMediaWithRelatedObjectIdAndFileTypeOptional(relatedObjectId, FileType.PROFILE_PIC);
    }

    public Optional<AccessibleMediaDto> retrieveAccessibleMediaWithRelatedObjectIdAndFileTypeOptional(String relatedObjectId, FileType fileType) {
        log.info("Retrieve profile picture optional has started. relatedObjectId: {}, fileType: {}", relatedObjectId, fileType);
        return mediaRepository.findFirstByRelatedObjectIdAndFileTypeAndPassiveIdIsNullOrderByCreatedDateDesc(relatedObjectId, fileType)
                .map(accessibleMediaDtoConverter::mapToAccessibleMediaDto);
    }

    public MediaDto retrieveMediaWithMediaIdAndRelatedObjectId(String mediaId, String relatedObjectId) {
        log.info("Retrieve media with key has started. mediaId: {}, relatedObjectId: {}", mediaId, relatedObjectId);
        return mediaRepository.findByMediaIdAndRelatedObjectIdAndUploadStatusAndPassiveIdIsNull(mediaId, relatedObjectId, MediaFileUploadStatusType.COMPLETED)
                .map(mediaDtoConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public AccessibleMediaDto retrieveAccessibleMediaWithMediaIdAndRelatedObjectId(String mediaId, String relatedObjectId, MediaFileUploadStatusType uploadStatus) {
        log.info("Retrieve media with key has started. mediaId: {}, relatedObjectId: {}", mediaId, relatedObjectId);
        return mediaRepository.findByMediaIdAndRelatedObjectIdAndUploadStatusAndPassiveIdIsNull(mediaId, relatedObjectId, uploadStatus)
                .map(accessibleMediaDtoConverter::mapToAccessibleMediaDto)
                .orElseThrow(NotFoundException::new);
    }

    public List<MediaDto> retrieveAllByRelatedObject(String relatedObjectId, FileType fileType) {
        log.info("Retrieve all by related object has started. relatedObjectId: {}, fileType: {}", relatedObjectId, fileType);
        return mediaRepository.findAllByRelatedObjectIdAndFileTypeAndUploadStatusAndPassiveIdIsNullOrderByCreatedDateAsc(relatedObjectId, fileType, MediaFileUploadStatusType.COMPLETED)
                .stream()
                .map(mediaDtoConverter::map)
                .toList();
    }

    public String retrievePublicDownloadLink(AccessibleMediaDto accessibleMediaDto) {
        log.info("Retrieve public download link for accessible media has started. accessibleMediaDto: {}", accessibleMediaDto);
        MediaFileOperationStrategy mediaFileOperationStrategy = mediaFileOperationServiceFactory.getStrategy(accessibleMediaDto.getProviderType());
        return mediaFileOperationStrategy.getFullPath(accessibleMediaDto.getBucketName(), accessibleMediaDto.getMediaOwnerType(), accessibleMediaDto.getRelatedObjectId(), accessibleMediaDto.getFileType(), accessibleMediaDto.getMediaKey(), accessibleMediaDto.getOriginalName());
    }

    public URL retrievePresignedPublicDownloadLink(AccessibleMediaDto accessibleMediaDto) {
        log.info("Retrieve presigned public download link has started. accessibleMediaDto: {}", accessibleMediaDto);
        MediaFileOperationStrategy mediaFileOperationStrategy = mediaFileOperationServiceFactory.getStrategy(accessibleMediaDto.getProviderType());
        return mediaFileOperationStrategy.generatePresignedDownloadUrl(accessibleMediaDto.getBucketName(), accessibleMediaDto.getStoragePath());
    }

    public List<String> retrieveAllTemporaryPublicAndExpired() {
        ZonedDateTime now = ZonedDateTime.now();
        log.info("Retrieve all temporary public and expired media has started. now: {}", now);
        return mediaRepository.findAllByVisibilityAndPublicUntilBeforeAndPassiveIdIsNull(MediaVisibilityType.TEMP_PUBLIC, now)
                .stream()
                .map(Media::getMediaId)
                .toList();
    }

    public MediaDto retrieveMediaWithMediaIdAndRelatedObjectIdIncludingPassive(String mediaId, String relatedObjectId) {
        log.info("Retrieve media with key has started. mediaId: {}, relatedObjectId: {}", mediaId, relatedObjectId);
        return mediaRepository.findByMediaIdAndRelatedObjectIdAndUploadStatus(mediaId, relatedObjectId, MediaFileUploadStatusType.COMPLETED)
                .map(mediaDtoConverter::map)
                .orElseThrow(NotFoundException::new);
    }

    public Optional<Media> retrieveEntityWithRelatedObjectIdAndMediaKey(String relatedObjectId, String mediaKey) {
        return mediaRepository.findFirstByRelatedObjectIdAndMediaKeyAndPassiveIdIsNull(relatedObjectId,mediaKey);
    }

    public boolean checkMediaRelatedWithRelatedObjectId(String mediaId, String relatedObjectId){
        log.info("Check media related with related object has started. mediaId: {}, relatedObjectId: {}", mediaId, relatedObjectId);
        return mediaRepository.existsByMediaIdAndRelatedObjectIdAndPassiveIdIsNull(mediaId, relatedObjectId);
    }

    public Page<Media> retrieveWaitingForUploadMedia(){
        log.info("Retrieve waiting for upload media has started.");
        return mediaRepository.findAllByUploadMethodAndUploadStatusAndPassiveIdIsNullOrderByCreatedDateDesc(MediaFileUploadMethodType.PRESIGNED_URL, MediaFileUploadStatusType.WAITING, PageRequest.of(0, 100));
    }
}
