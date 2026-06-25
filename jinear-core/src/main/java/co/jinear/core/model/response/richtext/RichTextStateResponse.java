package co.jinear.core.model.response.richtext;

import co.jinear.core.model.dto.richtext.RichTextStateDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class RichTextStateResponse extends BaseResponse {

    @JsonProperty("data")
    private RichTextStateDto richTextStateDto;
}
