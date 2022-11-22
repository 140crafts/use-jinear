package co.jinear.core.model.vo.media;

import co.jinear.core.model.enumtype.media.FileType;
import co.jinear.core.model.enumtype.media.MediaOwnerType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class InitializeMediaVo {
    private String ownerId;
    private String relatedObjectId;
    private MultipartFile file;
    private FileType fileType;
    private MediaOwnerType mediaOwnerType;
}
