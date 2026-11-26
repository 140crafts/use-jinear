package co.jinear.core.system.gcloud.auth.model.response;

import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthTokenResponse {

    private String accessToken;
    private Integer expiresIn;
    private String refreshToken;
    private String scope;
    private String tokenType;
    private String idToken;
}
