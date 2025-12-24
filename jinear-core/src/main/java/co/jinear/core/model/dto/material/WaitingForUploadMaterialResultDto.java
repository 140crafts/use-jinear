package co.jinear.core.model.dto.material;

import co.jinear.core.model.dto.media.WaitingMediaResultDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WaitingForUploadMaterialResultDto extends WaitingMediaResultDto {

    private String materialId;

    public WaitingForUploadMaterialResultDto(String materialId, WaitingMediaResultDto waitingMediaResultDto) {
        super(waitingMediaResultDto.getPresignedUrl(), waitingMediaResultDto.getMediaId(), waitingMediaResultDto.getUploadWindowExpiresAt());
        this.materialId = materialId;
    }
}
