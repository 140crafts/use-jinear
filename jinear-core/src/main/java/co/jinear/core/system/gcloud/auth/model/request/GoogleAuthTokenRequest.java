package co.jinear.core.system.gcloud.auth.model.request;

import co.jinear.core.system.gcloud.auth.model.enumtype.GrantType;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleAuthTokenRequest extends GoogleAuthTokenBaseRequest {

    private String code;
    private String redirectUri;
    private GrantType grantType = GrantType.AUTHORIZATION_CODE;
}
