package co.jinear.core.system;

import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class FileStorageUtils {
    public static final String GCLOUD_PREFIX = "https://storage.googleapis.com/";
    public static final String FS_PREFIX = "FILE_STORAGE";

    public static String generatePath(MediaOwnerType mediaOwnerType, String relatedObjectId, FileType fileType, String mediaKey, String originalName) {
        final StringBuilder sb = new StringBuilder();
        sb.append(FileStorageUtils.FS_PREFIX)
                .append(java.io.File.separator).append(mediaOwnerType.name())
                .append(java.io.File.separator).append(relatedObjectId)
                .append(java.io.File.separator).append(fileType.name())
                .append(java.io.File.separator).append(mediaKey);
        if (Objects.nonNull(originalName)) {
            sb.append(java.io.File.separator).append(originalName);
        }
        return sb.toString();
    }

    public static String generateFullPath(String bucketName, MediaOwnerType mediaOwnerType, String relatedObjectId, FileType fileType, String mediaKey, String originalName) {
        return GCLOUD_PREFIX + bucketName + java.io.File.separator + generatePath(mediaOwnerType, relatedObjectId, fileType, mediaKey, originalName);
    }
}
