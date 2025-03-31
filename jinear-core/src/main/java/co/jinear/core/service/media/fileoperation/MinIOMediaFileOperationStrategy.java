package co.jinear.core.service.media.fileoperation;

import co.jinear.core.config.properties.MinIoProperties;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaFileProviderType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import co.jinear.core.model.vo.media.MediaInitializeResultVo;
import io.minio.*;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static co.jinear.core.system.FileStorageUtils.generatePath;

@Slf4j
@Service
@ConditionalOnProperty(value = "file-storage.minio.enabled", havingValue = "true")
public class MinIOMediaFileOperationStrategy implements MediaFileOperationStrategy {

    private final MinIoProperties minIoProperties;
    private final MinioClient minioClient;

    public MinIOMediaFileOperationStrategy(MinIoProperties minIoProperties) throws Exception {
        this.minIoProperties = minIoProperties;
        this.minioClient = MinioClient.builder()
                .endpoint(minIoProperties.getEndpoint())
                .credentials(minIoProperties.getKey(), minIoProperties.getSecret())
                .build();
        checkAndInitializeBucket(minioClient, minIoProperties.getPublicBucketName(), Boolean.TRUE);
        checkAndInitializeBucket(minioClient, minIoProperties.getPrivateBucketName(), Boolean.FALSE);
    }

    @Override
    public MediaInitializeResultVo save(MultipartFile file, String path) {
        String bucketName = minIoProperties.getPrivateBucketName();
        try {
            PutObjectArgs.Builder putObjectArgsBuilder = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path)
                    .stream(file.getInputStream(), file.getSize(), -1);

            Optional.of(file)
                    .map(MultipartFile::getContentType)
                    .ifPresent(putObjectArgsBuilder::contentType);
            minioClient.putObject(putObjectArgsBuilder.build());
            return new MediaInitializeResultVo(bucketName);
        } catch (Exception e) {
            log.error("Save media to storage has failed.", e);
            throw new BusinessException();
        }
    }

    @Override
    public void remove(String bucketName, String path) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path)
                    .build());
        } catch (Exception e) {
            log.error("Save media to storage has failed.", e);
            throw new BusinessException();
        }
    }

    @Override
    public String makePublic(String bucketName, String path) {
        if (minIoProperties.getPublicBucketName().equals(bucketName)) {
            log.info("Already public");
            return bucketName;
        }
        moveObject(minIoProperties.getPrivateBucketName(), minIoProperties.getPublicBucketName(), path);
        return minIoProperties.getPublicBucketName();
    }

    @Override
    public String makePrivate(String bucketName, String path) {
        if (minIoProperties.getPrivateBucketName().equals(bucketName)) {
            log.info("Already private");
            return bucketName;
        }
        moveObject(minIoProperties.getPublicBucketName(), minIoProperties.getPrivateBucketName(), path);
        return minIoProperties.getPrivateBucketName();
    }

    @Override
    public String getFullPath(String bucketName, MediaOwnerType mediaOwnerType, String relatedObjectId, FileType fileType, String mediaKey, String originalName) {
        return minIoProperties.getBasePath() + bucketName + java.io.File.separator + generatePath(mediaOwnerType, relatedObjectId, fileType, mediaKey, originalName);
    }

    @Override
    public MediaFileProviderType getType() {
        return MediaFileProviderType.MINIO;
    }

    private boolean moveObject(String sourceBucket, String destBucket, String object) {
        try {
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .source(CopySource.builder()
                                    .bucket(sourceBucket)
                                    .object(object)
                                    .build())
                            .bucket(destBucket)
                            .object(object)
                            .build());
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(sourceBucket)
                            .object(object)
                            .build());
            return true;
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Error moving object", e);
            return false;
        }
    }


    private void checkAndInitializeBucket(MinioClient minioClient, String bucketName, boolean isPublic) throws Exception {
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!bucketExists) {
            log.info("Creating bucket {}", bucketName);
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            if (isPublic) {
                log.info("Creating public policy");
                String policy = String.format("""
                             {
                                 "Version": "2012-10-17",
                                 "Statement": [
                                     {
                                         "Effect": "Allow",
                                         "Principal": {
                                             "AWS": ["*"]
                                         },
                                         "Action": ["s3:GetObject"],
                                         "Resource": ["arn:aws:s3:::%s/*"]
                                     }
                                 ]
                             }
                        """, bucketName);
                minioClient.setBucketPolicy(
                        SetBucketPolicyArgs.builder()
                                .bucket(bucketName)
                                .config(policy)
                                .build()
                );
            }
        }
    }
}
