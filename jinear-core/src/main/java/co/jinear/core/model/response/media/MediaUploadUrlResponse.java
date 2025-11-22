package co.jinear.core.model.response.media;

import co.jinear.core.model.dto.media.WaitingMediaResultDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediaUploadUrlResponse extends BaseResponse {

    @JsonProperty("data")
    private WaitingMediaResultDto waitingMediaResultDto;
}
