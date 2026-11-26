package co.jinear.core.model.response.richtext;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.richtext.RichTextVersionDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RichTextVersionPaginatedResponse extends BaseResponse {

    @JsonProperty("data")
    private PageDto<RichTextVersionDto> richTextVersionDtoPageDto;
}
