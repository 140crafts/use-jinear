package co.jinear.core.model.request.oauth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The RFC 6749 token endpoint form.
 * <p>
 * The wire names are snake_case and this body arrives as form data, which Spring binds by
 * JavaBean property name. Spring Framework 6.0 has no {@code @BindParam}, so each wire name
 * gets a setter alias here and the rest of the codebase sees ordinary camelCase accessors.
 */
@Getter
@Setter
@ToString(exclude = {"code", "codeVerifier", "refreshToken"})
public class OauthTokenRequest {

    private String grantType;
    private String code;
    private String redirectUri;
    private String clientId;
    private String codeVerifier;
    private String refreshToken;
    private String scope;
    private String resource;

    public void setGrant_type(String grantType) {
        this.grantType = grantType;
    }

    public void setRedirect_uri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public void setClient_id(String clientId) {
        this.clientId = clientId;
    }

    public void setCode_verifier(String codeVerifier) {
        this.codeVerifier = codeVerifier;
    }

    public void setRefresh_token(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
