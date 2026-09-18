package co.jinear.core.model.response.oauth;

import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.oauth.OauthClientDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OauthClientListingResponse extends BaseResponse {

    @JsonProperty("data")
    private PageDto<OauthClientDto> oauthClientDtoPage;
}
