package co.jinear.core.model.vo.media;

import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaFileOwnershipStatusType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseInitializeMediaVo {
    private String ownerId;
    private String relatedObjectId;
    private FileType fileType;
    private MediaOwnerType mediaOwnerType;
    private MediaVisibilityType visibility;
    private MediaFileOwnershipStatusType ownershipStatus = MediaFileOwnershipStatusType.OWNED;
}
