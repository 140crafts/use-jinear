package co.jinear.core.model.response.oauth;

import co.jinear.core.model.dto.oauth.OauthConnectionDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OauthConnectionListingResponse extends BaseResponse {

    @JsonProperty("data")
    private List<OauthConnectionDto> oauthConnectionDtoList;
}
