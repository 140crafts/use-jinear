package co.jinear.core.model.response.richtext;

import co.jinear.core.model.dto.media.AccessibleMediaDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RichTextTempImageResponse extends BaseResponse {

    @JsonProperty("data")
    private AccessibleMediaDto accessibleMediaDto;
}
