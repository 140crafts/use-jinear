package co.jinear.core.model.request.oauth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The RFC 7009 revocation endpoint form.
 */
@Getter
@Setter
@ToString(exclude = "token")
public class OauthRevokeRequest {

    private String token;
    private String tokenTypeHint;

    public void setToken_type_hint(String tokenTypeHint) {
        this.tokenTypeHint = tokenTypeHint;
    }
}
