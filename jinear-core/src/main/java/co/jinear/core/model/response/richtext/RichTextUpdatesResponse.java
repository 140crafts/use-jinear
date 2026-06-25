package co.jinear.core.model.response.richtext;

import co.jinear.core.model.dto.richtext.RichTextUpdatesDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RichTextUpdatesResponse extends BaseResponse {

    @JsonProperty("data")
    private RichTextUpdatesDto richTextUpdatesDto;
}
