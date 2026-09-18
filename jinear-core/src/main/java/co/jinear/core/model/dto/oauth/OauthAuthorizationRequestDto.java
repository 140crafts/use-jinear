package co.jinear.core.model.dto.oauth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class OauthAuthorizationRequestDto {

    private String oauthAuthorizationRequestId;
    private String clientId;
    private String redirectUri;
    private String scope;
    private String state;
    private String codeChallenge;
    private String codeChallengeMethod;
    private String resource;
    private Date expiresAt;
}
