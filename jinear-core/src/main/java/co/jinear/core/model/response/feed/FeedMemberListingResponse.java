package co.jinear.core.model.response.feed;

import co.jinear.core.model.dto.feed.FeedMemberDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeedMemberListingResponse extends BaseResponse {

    @JsonProperty("data")
    private List<FeedMemberDto> feedMemberDtos;
}
