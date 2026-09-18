package co.jinear.core.model.dto.oauth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class OauthRefreshTokenDto {

    private String oauthRefreshTokenId;
    private String oauthConnectionId;
    private Date expiresAt;
}
