package co.jinear.core.model.dto.media;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaFileProviderType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class MediaDto extends BaseDto {
    private String mediaId;
    private String ownerId;
    private String relatedObjectId;
    private MediaOwnerType mediaOwnerType;
    private FileType fileType;
    private String bucketName;
    private String originalName;
    private Long size;
    @Nullable
    private String contentType;
    private MediaFileProviderType providerType;
    private String url;
    private MediaVisibilityType visibility;
}