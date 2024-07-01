package co.jinear.core.service.media.fileoperation;

import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaFileProviderType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import co.jinear.core.model.vo.media.MediaInitializeResultVo;
import org.springframework.web.multipart.MultipartFile;

public interface MediaFileOperationStrategy {

    MediaInitializeResultVo save(MultipartFile file, String path);

    void remove(String bucketName, String path);

    void makePublic(String bucketName, String path);

    void makePrivate(String bucketName, String path);

    String getFullPath(String bucketName, MediaOwnerType mediaOwnerType, String relatedObjectId, FileType fileType, String mediaKey, String originalName);

    MediaFileProviderType getType();
}
