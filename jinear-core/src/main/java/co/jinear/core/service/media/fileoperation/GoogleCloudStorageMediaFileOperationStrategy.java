package co.jinear.core.service.media.fileoperation;

import co.jinear.core.config.properties.GCloudProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaFileProviderType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.vo.media.MediaInitializeResultVo;
import co.jinear.core.model.vo.media.WaitingMediaResultVo;
import co.jinear.core.system.gcloud.storage.CloudStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static co.jinear.core.system.FileStorageUtils.generatePath;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleCloudStorageMediaFileOperationStrategy implements MediaFileOperationStrategy {

    private final GCloudProperties gCloudProperties;

    @Override
    public MediaInitializeResultVo save(MultipartFile file, MediaVisibilityType visibility, String path) {
        String bucketName = gCloudProperties.getBucketName();
        log.info("Save media to storage has started. bucketName: {}, path: {}", bucketName, path);
        try {
            CloudStorage.uploadObject(bucketName, path, file);
            if (MediaVisibilityType.PUBLIC.equals(visibility)) {
                CloudStorage.makeObjectPublic(bucketName, path);
            }
            return new MediaInitializeResultVo(bucketName);
        } catch (Exception e) {
            log.error("Save media to storage has failed.", e);
            throw new BusinessException();
        }
    }

    @Override
    public WaitingMediaResultVo presignUrl(String path, String contentType, MediaVisibilityType visibility, long fileSizeInBytes) {
        String bucketName = gCloudProperties.getBucketName();
        log.info("Presign url has started. path: {}, contentType: {}", path, contentType);
        URL url = CloudStorage.generateV4PutSignedUrl(bucketName, path, contentType, gCloudProperties.getSignedUrlDuration(), fileSizeInBytes);
        WaitingMediaResultVo waitingMediaResultVo = new WaitingMediaResultVo(url, ZonedDateTime.now().plus(gCloudProperties.getSignedUrlDuration(), ChronoUnit.MILLIS));
        waitingMediaResultVo.setBucketName(bucketName);
        return waitingMediaResultVo;
    }

    @Override
    public URL generatePresignedDownloadUrl(String bucketName, String path) {
        log.info("Generating presigned GET URL for object [{}] in bucket [{}]", path, bucketName);
        try {
            return CloudStorage.generateV4GetSignedUrl(bucketName, path, gCloudProperties.getSignedUrlDuration());
        } catch (Exception e) {
            log.error("Error generating download url for path: {}", path, e);
            throw new BusinessException("Could not generate download URL.");
        }
    }

    @Override
    public boolean doesObjectExists(String bucketName, String path) {
        return CloudStorage.doesObjectExists(bucketName, path);
    }

    @Override
    public void remove(String bucketName, String path) {
        log.info("Remove media from storage has started. bucketName: {}, path: {}", bucketName, path);
        try {
            CloudStorage.deleteObject(bucketName, path);
        } catch (Exception e) {
            log.info("Remove media from storage has failed.", e);
            throw new BusinessException();
        }
    }

    @Override
    public String makePublic(String bucketName, String path) {
        log.info("Make media public has started. bucketName: {}, path: {}", bucketName, path);
        try {
            CloudStorage.makeObjectPublic(bucketName, path);
        } catch (Exception e) {
            log.info("Make media public has failed.", e);
            throw new BusinessException();
        }
        return bucketName;
    }

    @Override
    public String makePrivate(String bucketName, String path) {
        log.info("Make media private has started. bucketName: {}, path: {}", bucketName, path);
        try {
            CloudStorage.makeObjectPrivate(bucketName, path);
        } catch (Exception e) {
            log.info("Make media private has failed.", e);
            throw new BusinessException();
        }
        return bucketName;
    }

    @Override
    public String getFullPath(String bucketName, MediaOwnerType mediaOwnerType, String relatedObjectId, FileType fileType, String mediaKey, String originalName) {
        return gCloudProperties.getCloudStorageBasePath() + bucketName + java.io.File.separator + generatePath(mediaOwnerType, relatedObjectId, fileType, mediaKey, originalName);
    }

    @Override
    public MediaFileProviderType getType() {
        return MediaFileProviderType.GCLOUD;
    }
}
