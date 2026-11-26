package co.jinear.core.system.gcloud.auth.model.request;

import co.jinear.core.system.gcloud.auth.model.enumtype.AccessType;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleAuthTokenBaseRequest {

    private String clientId;
    private String clientSecret;
    private AccessType accessType = AccessType.OFFLINE;
}
