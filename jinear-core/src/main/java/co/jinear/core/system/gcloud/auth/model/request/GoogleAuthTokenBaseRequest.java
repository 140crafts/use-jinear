package co.jinear.core.system.gcloud.auth.model.request;

import co.jinear.core.system.gcloud.auth.model.enumtype.AccessType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleAuthTokenBaseRequest {

    private String clientId;
    private String clientSecret;
    private AccessType accessType = AccessType.OFFLINE;
}
