package co.jinear.core.model.dto.oauth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OauthConsentInfoDto {

    private String requestId;
    private String clientDisplayHost;
    private String clientName;
    private String clientUri;
    private String logoUri;
    private String policyUri;
    private String tosUri;
    private String redirectHost;
    private Boolean loopbackOnly;
    private List<String> requestedScopes;
}
