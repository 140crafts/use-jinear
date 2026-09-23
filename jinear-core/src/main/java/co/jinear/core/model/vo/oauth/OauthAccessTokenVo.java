package co.jinear.core.model.vo.oauth;

import co.jinear.core.model.vo.auth.SessionCarrier;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Set;

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
