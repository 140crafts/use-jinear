package co.jinear.core.model.response.richtext;

import co.jinear.core.model.dto.richtext.RichTextVersionDetailDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RichTextVersionResponse extends BaseResponse {

    @JsonProperty("data")
    private RichTextVersionDetailDto richTextVersionDetailDto;
}
