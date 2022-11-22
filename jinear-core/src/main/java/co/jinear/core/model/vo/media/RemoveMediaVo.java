package co.jinear.core.model.vo.media;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RemoveMediaVo {
    private String responsibleAccountId;
    private String mediaId;
}
