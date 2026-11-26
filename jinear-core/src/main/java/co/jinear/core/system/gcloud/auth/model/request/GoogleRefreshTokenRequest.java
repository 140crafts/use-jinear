package co.jinear.core.system.gcloud.auth.model.request;

import co.jinear.core.system.gcloud.auth.model.enumtype.GrantType;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleRefreshTokenRequest extends GoogleAuthTokenBaseRequest {

    private String refreshToken;
    private GrantType grantType = GrantType.REFRESH_TOKEN;
}
