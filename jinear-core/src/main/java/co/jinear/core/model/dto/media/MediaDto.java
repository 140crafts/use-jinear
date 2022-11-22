package co.jinear.core.model.dto.media;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediaDto extends BaseDto {
    private String mediaId;
    private String ownerId;
    private String relatedObjectId;
    private MediaOwnerType mediaOwnerType;
    private FileType fileType;
    private String bucketName;
    private String storagePath;
    private String originalName;
}