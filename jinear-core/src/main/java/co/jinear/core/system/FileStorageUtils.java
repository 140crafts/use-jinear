package co.jinear.core.system;

import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileStorageUtils {
    public static final String GCLOUD_PREFIX = "https://storage.googleapis.com/";
    public static final String FS_PREFIX = "FILE_STORAGE";

    public static String generatePath(MediaOwnerType mediaOwnerType, String relatedObjectId, FileType fileType, String mediaKey) {
        final StringBuilder sb = new StringBuilder();
        return sb.append(FileStorageUtils.FS_PREFIX)
                .append(java.io.File.separator).append(mediaOwnerType.name())
                .append(java.io.File.separator).append(relatedObjectId)
                .append(java.io.File.separator).append(fileType.name())
                .append(java.io.File.separator).append(mediaKey)
                .toString();
    }

    public static String generateFullPath(String bucketName, MediaOwnerType mediaOwnerType, String relatedObjectId, FileType fileType, String mediaKey) {
        return GCLOUD_PREFIX + bucketName + java.io.File.separator + generatePath(mediaOwnerType, relatedObjectId, fileType, mediaKey);
    }
}
