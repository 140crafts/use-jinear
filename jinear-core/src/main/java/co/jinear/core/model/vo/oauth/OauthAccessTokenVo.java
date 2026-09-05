package co.jinear.core.model.vo.oauth;

import co.jinear.core.model.vo.auth.SessionCarrier;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Set;

/**
 * The identity behind one request authenticated by an OAuth bearer token.
 * <p>
 * Carried as the Authentication details rather than as the credential, because the
 * credential slot is where SessionInfoService expects a parseable session JWT and an
 * this token is signed with a different key.
 */
@Getter
@Setter
@ToString
public class OauthAccessTokenVo implements SessionCarrier {

    private String accountId;
    private String connectionId;
    private String clientId;
    private String sessionInfoId;
    private Set<String> scopes;
    private Date expiresAt;

    @Override
    public String sessionInfoId() {
        return sessionInfoId;
    }
}
