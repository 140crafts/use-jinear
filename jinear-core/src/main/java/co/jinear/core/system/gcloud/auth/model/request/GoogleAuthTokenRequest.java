package co.jinear.core.system.gcloud.auth.model.request;

import co.jinear.core.system.gcloud.auth.model.enumtype.GrantType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleAuthTokenRequest extends GoogleAuthTokenBaseRequest {

    private String code;
    private String redirectUri;
    private GrantType grantType = GrantType.AUTHORIZATION_CODE;
}
