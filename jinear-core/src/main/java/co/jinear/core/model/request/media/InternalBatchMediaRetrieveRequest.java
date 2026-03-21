package co.jinear.core.model.request.media;

import co.jinear.core.model.enumtype.media.FileType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
public class InternalBatchMediaRetrieveRequest {
    private Set<String> relatedObjectIds;
    private List<FileType> fileTypes;
}
