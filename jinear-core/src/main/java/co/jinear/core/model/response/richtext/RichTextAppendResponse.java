package co.jinear.core.model.response.richtext;

import co.jinear.core.model.dto.richtext.RichTextAppendDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class RichTextAppendResponse extends BaseResponse {

    @JsonProperty("data")
    private RichTextAppendDto richTextAppendDto;
}
