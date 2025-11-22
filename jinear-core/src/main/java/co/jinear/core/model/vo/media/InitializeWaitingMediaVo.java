package co.jinear.core.model.vo.media;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InitializeWaitingMediaVo extends BaseInitializeMediaVo {

    private String originalName;
    private String contentType;
    private long fileSize;
}
