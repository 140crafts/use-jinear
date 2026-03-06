package co.jinear.core.service.media.fileoperation;

import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaFileProviderType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.vo.media.MediaInitializeResultVo;
import co.jinear.core.model.vo.media.WaitingMediaResultVo;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

public interface MediaFileOperationStrategy {

    MediaInitializeResultVo save(MultipartFile file, MediaVisibilityType visibility, String path);

    MediaInitializeResultVo save(URL url, String path, String contentType);

    WaitingMediaResultVo presignUrl(String path, String contentType, MediaVisibilityType visibility, long fileSizeInBytes);

    URL generatePresignedDownloadUrl(String bucketName, String path);

    boolean doesObjectExists(String bucketName, String path);

    void remove(String bucketName, String path);

    String makePublic(String bucketName, String path);

    String makePrivate(String bucketName, String path);

    String getFullPath(String bucketName, MediaOwnerType mediaOwnerType, String relatedObjectId, FileType fileType, String mediaKey, String originalName);

    MediaFileProviderType getType();
}
