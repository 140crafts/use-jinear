package co.jinear.core.model.response.feed;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.feed.FeedMemberDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedMemberPaginatedResponse extends BaseResponse {

    @JsonProperty("data")
    private PageDto<FeedMemberDto> feedMemberDtoPage;
}
