package co.jinear.core.model.response.media;

import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class InternalMediaInitializeResponse extends BaseResponse {
    @JsonProperty("data")
    private AccessibleMediaDto media;
}
