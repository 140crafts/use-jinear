package co.jinear.core.model.request.oauth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The RFC 6749 authorization endpoint query string, with the RFC 8707 resource indicator.
 * Wire names are snake_case, see {@link OauthTokenRequest} for why the aliases exist.
 */
@Getter
@Setter
@ToString
public class OauthAuthorizeRequest {

    private String responseType;
    private String clientId;
    private String redirectUri;
    private String scope;
    private String state;
    private String codeChallenge;
    private String codeChallengeMethod;
    private String resource;

    public void setResponse_type(String responseType) {
        this.responseType = responseType;
    }

    public void setClient_id(String clientId) {
        this.clientId = clientId;
    }

    public void setRedirect_uri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public void setCode_challenge(String codeChallenge) {
        this.codeChallenge = codeChallenge;
    }

    public void setCode_challenge_method(String codeChallengeMethod) {
        this.codeChallengeMethod = codeChallengeMethod;
    }
}
