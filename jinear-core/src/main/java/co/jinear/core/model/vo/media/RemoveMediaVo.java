package co.jinear.core.model.vo.media;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RemoveMediaVo {
    private String responsibleAccountId;
    private String mediaId;
    private String existingPassiveId;

    public RemoveMediaVo(String responsibleAccountId, String mediaId) {
        this.responsibleAccountId = responsibleAccountId;
        this.mediaId = mediaId;
    }
}
