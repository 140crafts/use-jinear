package co.jinear.core.model.response.oauth;

import co.jinear.core.model.dto.oauth.OauthConsentInfoDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OauthConsentInfoResponse extends BaseResponse {

    @JsonProperty("data")
    private OauthConsentInfoDto oauthConsentInfoDto;
}
