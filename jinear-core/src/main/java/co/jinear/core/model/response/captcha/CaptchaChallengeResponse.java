package co.jinear.core.model.response.captcha;

import co.jinear.core.model.dto.captcha.CaptchaChallengeDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaptchaChallengeResponse extends BaseResponse {

    @JsonProperty("data")
    private CaptchaChallengeDto captchaChallengeDto;
}
