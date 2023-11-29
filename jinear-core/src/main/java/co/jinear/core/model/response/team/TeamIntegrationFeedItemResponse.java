package co.jinear.core.model.response.team;

import co.jinear.core.model.dto.integration.FeedItemDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamIntegrationFeedItemResponse extends BaseResponse {

    @JsonProperty("data")
    private FeedItemDto feedItemDto;
}
