package co.jinear.core.model.dto.oauth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString(exclude = "codeChallenge")
public class OauthAuthorizationCodeDto {

    private String oauthAuthorizationCodeId;
    private String accountId;
    private String clientId;
    private String oauthConnectionId;
    private String redirectUri;
    private String scope;
    private String codeChallenge;
    private String codeChallengeMethod;
    private String resource;
    private Date expiresAt;
}
