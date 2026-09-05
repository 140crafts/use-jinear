package co.jinear.core.model.response.oauth;

import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OauthConsentResponse extends BaseResponse {

    /** Where the browser must be sent to hand the code, or the error, back to the client. */
    @JsonProperty("data")
    private String redirectUrl;
}
